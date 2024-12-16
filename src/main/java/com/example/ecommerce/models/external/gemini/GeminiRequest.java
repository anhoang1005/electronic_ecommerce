package com.example.ecommerce.models.external.gemini;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class GeminiRequest {
    private List<Content> contents;

    public void setGeminiData(String message, String mime_type, String base64File) {
        Part part = new Part();
        if (message != null) {
            part.setText(message);
        }
        if (mime_type != null) {
            part.setInline_data(InlineData.builder()
                    .mime_type(mime_type)
                    .data(base64File)
                    .build());
        }
        Content content = new Content();
        content.setParts(Collections.singletonList(part));
        this.contents = Collections.singletonList(content);
    }

    @Getter
    @Setter
    public static class Content {
        private List<Part> parts;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Part {
        private String text;
        private InlineData inline_data;
    }

    @Getter
    @Setter
    @Builder
    public static class InlineData {
        private String mime_type;
        private String data;
    }

}