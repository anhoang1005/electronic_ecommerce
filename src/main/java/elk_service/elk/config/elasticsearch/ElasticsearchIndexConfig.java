package elk_service.elk.config.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Configuration
@Slf4j
public class ElasticsearchIndexConfig {

    @Autowired
    private RestHighLevelClient client;

    public ElasticsearchIndexConfig(RestHighLevelClient client) {
        this.client = client;
    }

//    @PostConstruct
    public void createProductDocumentIndex() {
        log.info("Created index");
        try {
            if (client.indices().exists(new GetIndexRequest("product-document"), RequestOptions.DEFAULT)) {
                client.indices().delete(new DeleteIndexRequest("product-document"), RequestOptions.DEFAULT);
            }
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("product-document");

            createIndexRequest.settings(Settings.builder()
                    .put("index.analysis.analyzer.autocomplete_analyzer.type", "custom")
                    .put("index.analysis.analyzer.autocomplete_analyzer.tokenizer", "autocomplete_tokenizer")
                    .putList("index.analysis.analyzer.autocomplete_analyzer.filter", "lowercase", "asciifolding")

                    // Cấu hình tokenizer `autocomplete_tokenizer`
                    .put("index.analysis.tokenizer.autocomplete_tokenizer.type", "edge_ngram")
                    .put("index.analysis.tokenizer.autocomplete_tokenizer.min_gram", 1)
                    .put("index.analysis.tokenizer.autocomplete_tokenizer.max_gram", 25)
                    .putList("index.analysis.tokenizer.autocomplete_tokenizer.token_chars", "letter", "digit")
            );

            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("title")
                    .field("type", "text")
                    .field("analyzer", "autocomplete_analyzer")  // Sử dụng analyzer tùy chỉnh
                    .endObject()
                    .startObject("categoryDocument")
                    .field("type", "object")
                    .startObject("properties")
                    .startObject("categoryName")
                    .field("type", "text")
                    .field("analyzer", "autocomplete_analyzer")  // Sử dụng analyzer tùy chỉnh
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("brandDocument")
                    .field("type", "object")
                    .startObject("properties")
                    .startObject("brandName")
                    .field("type", "text")
                    .field("analyzer", "autocomplete_analyzer")  // Sử dụng analyzer tùy chỉnh
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("attributes")
                    .field("type", "nested")  // Định nghĩa là nested
                    .startObject("properties")
                    .startObject("attributeId")
                    .field("type", "long")
                    .endObject()
                    .startObject("attributeName")
                    .field("type", "text")
                    .field("analyzer", "autocomplete_analyzer")  // Sử dụng analyzer tùy chỉnh
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type", "keyword")
                    .field("ignore_above", 256)
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("attributeValue")
                    .field("type", "text")
                    .field("analyzer", "autocomplete_analyzer")  // Sử dụng analyzer tùy chỉnh
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type", "keyword")
                    .field("ignore_above", 256)
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject();
            createIndexRequest.mapping(String.valueOf(mapping));
            client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            log.info("Created index");
        } catch (IOException e) {
            System.err.println("Failed to create index: " + e.getMessage());
        }
    }
}