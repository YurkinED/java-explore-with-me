package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.EndpointHitDto;

@Service
@Slf4j
public class EventsClient2 extends Client2 {
    private static final String API_PREFIX = "/hit";

    @Autowired
    public EventsClient2(@Value("${statistic-server.uri}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:9090/hit"))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postStatistic(EndpointHitDto endpointHit) {
        log.info("Get post statistic {}", endpointHit);
        return post("", endpointHit);
    }
}