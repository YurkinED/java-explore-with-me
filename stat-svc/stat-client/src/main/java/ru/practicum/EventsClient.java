package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
@Slf4j
public class EventsClient extends Client {
    private static final String API_PREFIX = "/hit";

    @Autowired
    public EventsClient(@Value("${statistic-server.uri}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        log.info("serverUrl + API_PREFIX={}",serverUrl + API_PREFIX );
    }

    public ResponseEntity<Object> postStatistic(EndpointHitDto endpointHit) {
        log.info("Get post statistic {}", endpointHit);
        return post("", endpointHit);
    }
}