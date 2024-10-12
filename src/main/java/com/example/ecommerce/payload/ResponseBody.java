package com.example.ecommerce.payload;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class ResponseBody {

    private int code;
    private String message;
    private Object data;
    private int status;

    public ResponseBody(Object data, Status status, Code code) {
        this.data = data;
        this.status = status.getValue();
        this.message = code.getMessage();
        this.code = code.getValue();
    }
    public ResponseBody(Object data, Status status, String message, Code code) {
        this.data = data;
        this.status = status.getValue();
        this.message = message;
        this.code = code.getValue();
    }

    public enum Status {
        SUCCESS(1),
        FAILED(0);
        private final int value;
        Status(int value) {
            this.value = value;
        }
        public int getValue() {
            return this.value;
        }
    }

    public enum Code {
        SUCCESS(200, "Successful"),
        CLIENT_ERROR(400, "Client error"),
        UNAUTHORIZED_REQUEST(401, "Unauthorized request"),
        FORBIDDEN(403, "Forbidden"),
        NOT_FOUND(404, "Not found"),
        TOKEN_NOT_REGISTER(3001, "Token not register"),
        INVALID_REQUEST_FORMAT(4010, "Invalid request format"),
        INTERNAL_ERROR(500, "Internal server error");
        private final int value;
        private final String message;
        Code(int value, String message) {
            this.value = value;
            this.message = message;
        }
        public String getMessage() {
            return this.message;
        }
        public int getValue() {
            return this.value;
        }
    }
}
