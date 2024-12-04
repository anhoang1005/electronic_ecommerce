package elk_service.elk.config.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${ELASTICSEARCH_HOST}")
    private String HOST;

    @Value("${ELASTICSEARCH_PORT}")
    private int PORT;

    @Value("${ELASTICSEARCH_SCHEME:http}")
    private String SCHEME;

    @Bean
    public RestHighLevelClient client() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(HOST, PORT, SCHEME));

        return new RestHighLevelClient(builder);
    }
}