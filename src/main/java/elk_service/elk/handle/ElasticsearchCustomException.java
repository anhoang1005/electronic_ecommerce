package elk_service.elk.handle;

public class ElasticsearchCustomException extends RuntimeException {
    public ElasticsearchCustomException(String message) {
        super(message);
    }
}
