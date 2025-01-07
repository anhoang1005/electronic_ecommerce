package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Prompt;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.models.external.gemini.GeminiDto;
import com.example.ecommerce.models.external.gemini.GeminiRequest;
import com.example.ecommerce.models.external.gemini.GeminiResponse;
import com.example.ecommerce.models.post.PostRequest;
import com.example.ecommerce.models.product_review.ProductReviewFilter;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.ProductReviewRepository;
import com.example.ecommerce.repository.PromptRepository;
import com.example.ecommerce.service.GeminiService;
import com.example.ecommerce.service.ProductReviewService;
import com.example.ecommerce.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class IGeminiService implements GeminiService {
    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;
    private final ProductReviewService productReviewService;
    private final ProductService productService;
    private final PromptRepository promptRepository;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String url;

    private final RestTemplate restTemplate;

    public IGeminiService(ProductRepository productRepository,
                          ProductReviewRepository productReviewRepository,
                          ProductReviewService productReviewService,
                          ProductService productService,
                          RestTemplate restTemplate,
                          @Value("${gemini.apikey}") String apiKey,
                          @Value("${gemini.api}") String url,
                          PromptRepository promptRepository,
                          ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.productReviewRepository = productReviewRepository;
        this.productReviewService = productReviewService;
        this.productService = productService;
        this.restTemplate = restTemplate;
        this.promptRepository = promptRepository;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.url = url;
    }

    @Override
    public ResponseBody chatWithGeminiText(String message) {
        try {
            //String jsonString = "{\"contents\":[{\"parts\":[{\"text\":\"" + message + "\"}]}]}";
            GeminiRequest req = new GeminiRequest();
            req.setGeminiData(message, null, null);
            String jsonString = objectMapper.writeValueAsString(req);
            System.out.println(jsonString);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

            // Gọi API POST với body
            ResponseEntity<String> response = restTemplate.exchange(
                    url + apiKey,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
                return new ResponseBody(dataObject, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
        return null;
    }

    @Override
    public ResponseBody chatWithGeminiImage(String message, MultipartFile file) {
        try {
            String jsonString;
            if (file != null) {
                if (message == null) message = "Mô tả ảnh";
                byte[] fileBytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(fileBytes);
                jsonString = "{ \"contents\": [{ \"parts\": [ {\"text\": \"" + message + "\"}, " +
                        "{\"inline_data\": {\"mime_type\": \"" + file.getContentType() + "\", \"data\": \"" + base64Image + "\"}}]}]}";
            } else {
                jsonString = "{\"contents\":[{\"parts\":[{\"text\":\"" + message + "\"}]}]}";
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url + apiKey,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
                return new ResponseBody(dataObject, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
        return null;
    }

    @Override
    public ResponseBody chatWithGeminiTextWithPrompt(GeminiDto dto) {
        try {
            String result;
            if (dto.getPromptName() != null) {
                Prompt promptText = promptRepository.findPromptByPromptName(dto.getPromptName())
                        .orElseThrow(() -> new RequestNotFoundException("ERROR"));
                result = promptText.getPromptText() + dto.getMessage();
            } else {
                result = dto.getMessage();
            }
            GeminiRequest req = new GeminiRequest();
            req.setGeminiData(result, null, null);
            String jsonString = objectMapper.writeValueAsString(req);
            System.out.println(jsonString);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

            // Goi API POST voi body
            ResponseEntity<String> response = restTemplate.exchange(
                    url + apiKey,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                //Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
                GeminiResponse response1 = objectMapper.readValue(response.getBody(), GeminiResponse.class);
                return new ResponseBody(
                        response1.getCandidates().get(0)
                                .getContent().getParts().get(0).getText(),
                        ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.warn("Request gemini not found");
            e.printStackTrace();
            throw new RequestNotFoundException("Request not found!");
        }
        return null;
    }

    @Override
    public ResponseBody getListPrompt() {
        try {
            List<Prompt> promptList = promptRepository.findAll();
            return new ResponseBody(promptList, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
    }


    @Override
    public Prompt getByPromptName(String promptName) {
        try {
            return promptRepository.findPromptByPromptName(promptName)
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
        } catch (Exception e) {
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
    }

    @Override
    public Prompt createPrompt(String promptName, String promptText) {
        try {
            Prompt prompt = new Prompt();
            prompt.setPromptName(promptName);
            prompt.setPromptText(promptText);
            prompt = promptRepository.save(prompt);
            return prompt;
        } catch (Exception e) {
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
    }

    @Override
    public Prompt updatePrompt(String promptName, String newPrompt, String promptText) {
        try {
            Prompt prompt = promptRepository.findPromptByPromptName(promptName)
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            prompt.setPromptName(newPrompt != null ? newPrompt : prompt.getPromptName());
            prompt.setPromptText(promptText != null ? promptText : prompt.getPromptText());
            prompt = promptRepository.save(prompt);
            return prompt;
        } catch (Exception e) {
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
    }

    @Override
    public ResponseBody deletePrompt(String promptName) {
        try {
            Prompt prompt = promptRepository.findPromptByPromptName(promptName)
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            promptRepository.delete(prompt);
            return new ResponseBody("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.warn("Request gemini not found");
            throw new RequestNotFoundException("Request not found!");
        }
    }

    @Override
    public ResponseBody reviewProduct(Long productId) {
        try {
            String text = "Bạn là một trợ lý AI hỗ trợ người dùng trên sàn thương mại điện tử Electrohub, phân tích dữ liệu JSON của sản phẩm khi người dùng nhấp vào và cung cấp tư vấn chi tiết. Bạn sẽ tóm tắt các đặc điểm nổi bật của sản phẩm, giải thích lợi ích và lý do sản phẩm này phù hợp với nhu cầu của người dùng. Đồng thời, nếu có dữ liệu, bạn sẽ so sánh ngắn gọn sản phẩm với các sản phẩm tương tự trong cùng danh mục, tóm tắt các đánh giá quan trọng từ người dùng trước đó, và đưa ra khuyến nghị mua hàng dựa trên thông tin sản phẩm, ưu đãi, hoặc lý do nên mua ngay, giúp người dùng đưa ra quyết định tốt nhất. Dữ liệu về sản phẩm là: ";
            ResponseBody responseBody = productService.getProductDetail(productId);
            String jsonString = objectMapper.writeValueAsString(responseBody.getData());
            return chatWithGeminiTextWithPromptText(text, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody aiGetReviewProduct(Long productId) {
        try {
            String text = "Bạn là một AI phân tích dữ liệu của sàn thương mại điện tử ElectroHub, chuyên đánh giá các sản phẩm điện tử dựa trên dữ liệu review của người dùng. Với dữ liệu đã được cung cấp, bạn sẽ:\n" +
                    "\n" +
                    "Phân tích và tổng hợp thông tin từ các đánh giá tích cực và tiêu cực.\n" +
                    "Đưa ra số liệu thống kê cụ thể như điểm trung bình, tỷ lệ đánh giá tốt/xấu.\n" +
                    "Đánh giá tổng quan: sản phẩm này có đáng mua hay không dựa trên phân tích trên.\n" +
                    "Trình bày ngắn gọn, xúc tích (dưới 500 ký tự), với nội dung liền mạch, không ngắt quãng. " +
                    "Dữ liệu về sản phẩm là: ";
            ProductReviewFilter filter = ProductReviewFilter.builder()
                    .productId(productId)
                    .rating(null)
                    .sort(null)
                    .pageNumber(1)
                    .pageSize(20)
                    .build();
            ResponseBody responseBody = productReviewService.guestGetListReviewOfProduct(filter);
            String jsonString = objectMapper.writeValueAsString(responseBody.getData());
            return chatWithGeminiTextWithPromptText(text, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody aiCompareProduct(Long productId1, Long productId2) {
        try {
            ResponseBody product1 = productService.getProductDetail(productId1);
            ResponseBody product2 = productService.getProductDetail(productId2);
            String productJson1 = objectMapper.writeValueAsString(product1.getData());
            String productJson2 = objectMapper.writeValueAsString(product2.getData());
            ProductReviewFilter filter1 = ProductReviewFilter.builder()
                    .productId(productId1)
                    .rating(null)
                    .sort(null)
                    .pageNumber(1)
                    .pageSize(20)
                    .build();
            ProductReviewFilter filter2 = ProductReviewFilter.builder()
                    .productId(productId2)
                    .rating(null)
                    .sort(null)
                    .pageNumber(1)
                    .pageSize(20)
                    .build();
            ResponseBody review1 = productReviewService.guestGetListReviewOfProduct(filter1);
            ResponseBody review2 = productReviewService.guestGetListReviewOfProduct(filter2);
            String reviewJson1 = objectMapper.writeValueAsString(review1.getData());
            String reviewJson2 = objectMapper.writeValueAsString(review2.getData());
            String text = "Bạn là một AI phân tích dữ liệu chuyên sâu về sàn thương mại điện tử ElectroHub, có nhiệm vụ đánh giá và so sánh các sản phẩm điện tử dựa trên dữ liệu chi tiết từ thông số kỹ thuật và phản hồi của người dùng. Với dữ liệu đã được cung cấp, bạn sẽ thực hiện các nhiệm vụ sau:\n" +
                    "\n" +
                    "1. Phân tích và so sánh chi tiết các thông số kỹ thuật của hai sản phẩm, chỉ ra sự khác biệt nổi bật về hiệu năng, thiết kế, tính năng và giá trị sử dụng.\n" +
                    "2. Đánh giá điểm số trung bình và sự phân bố điểm đánh giá (ví dụ: tỷ lệ 5 sao, 4 sao, v.v.) của từng sản phẩm.\n" +
                    "3. So sánh tỷ lệ đánh giá tích cực (đánh giá tốt) và tiêu cực (đánh giá xấu) của hai sản phẩm, đồng thời chỉ ra nguyên nhân chính dẫn đến sự khác biệt này nếu có.\n" +
                    "4. Đưa ra kết luận chi tiết, chỉ rõ sản phẩm nào có ưu điểm vượt trội, sản phẩm nào phù hợp hơn với các nhu cầu cụ thể của người dùng.\n" +
                    "\n" +
                    "Yêu cầu trình bày:\n" +
                    "- Nội dung phân tích rõ ràng, mạch lạc và có cấu trúc hợp lý.\n" +
                    "- Sử dụng không quá 700 ký tự, nhưng vẫn đảm bảo đầy đủ thông tin.\n" +
                    "- Cung cấp thông tin liền mạch, không ngắt quãng, dễ hiểu.\n" +
                    "\n" +
                    "Dữ liệu về sản phẩm 1 là: " + productJson1 + " - " + reviewJson1 + "\n" +
                    "Dữ liệu về sản phẩm 2 là: " + productJson2 + " - " + reviewJson2;
            return chatWithGeminiTextWithPromptText(text, "");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody aiCheckPostCreate(PostRequest req) {
        try {
            String json = objectMapper.writeValueAsString(req);
            String text = "Bạn là một AI chuyên kiểm tra nội dung dựa trên tiêu chuẩn cộng đồng của một nền tảng mạng xã hội. Với dữ liệu của bài đăng sau, hãy thực hiện các bước:\n" +
                    "\n" +
                    "Kiểm tra nội dung (content), thẻ hashtag (hashTag), vị trí (location), và vai trò của người đăng bài (postRole).\n" +
                    "Kiểm tra xem bài viết có vi phạm các tiêu chuẩn cộng đồng sau đây hay không:\n" +
                    "Nội dung có chứa ngôn từ gây thù hận, tục tĩu, bạo lực hoặc phân biệt đối xử.\n" +
                    "Hashtag có chứa từ khóa phản cảm, không phù hợp hoặc vi phạm quy định.\n" +
                    "Bài viết có thông tin sai lệch hoặc nội dung gây hiểu lầm.\n" +
                    "Kết luận:\n" +
                    "Nếu bài viết vi phạm tiêu chuẩn cộng đồng, chỉ trả về 1 chữ \"true\".\n" +
                    "Nếu không vi phạm, trả về chỉ 1 chữ \"false\".\n" +
                    "Nội dung bài đăng là : " + json;
            return chatWithGeminiTextWithPromptText(text, "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody chatWithGeminiTextWithPromptText(String promptText, String message) {
        try {
            String result;
            if (promptText != null) {
                result = promptText + message;
            } else {
                result = message;
            }
            GeminiRequest req = new GeminiRequest();
            req.setGeminiData(result, null, null);
            String jsonString = objectMapper.writeValueAsString(req);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

            // Goi API POST voi body
            ResponseEntity<String> response = restTemplate.exchange(
                    url + apiKey,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                //Object dataObject = objectMapper.readValue(response.getBody(), Object.class);
                GeminiResponse response1 = objectMapper.readValue(response.getBody(), GeminiResponse.class);
                return new ResponseBody(
                        response1.getCandidates().get(0)
                                .getContent().getParts().get(0).getText(),
                        ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.warn("Request gemini not found");
            e.printStackTrace();
            throw new RequestNotFoundException("Request not found!");
        }
        return null;
    }
}
