package com.example.ecommerce.config.elasticsearch;

import jakarta.annotation.PostConstruct;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.util.Collections;

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
