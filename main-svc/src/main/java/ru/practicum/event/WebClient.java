package ru.practicum.event;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
@Slf4j
public class WebClient {

    private final RestTemplate restTemplate; //= new RestTemplate();
    private final String statUrl;


    public WebClient(RestTemplate restTemplate, @Value("${statistic-server.uri}") String statUrl) {
        this.restTemplate = restTemplate;
        this.statUrl = statUrl;
    }

    public void addToStatistic(HttpServletRequest httpServletRequest) {
        EndpointHitDto hitDto = new EndpointHitDto();
        hitDto.setIp(httpServletRequest.getRemoteAddr());
        hitDto.setUri(httpServletRequest.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now());
        String url = statUrl + "/hit";
        log.info("---===>>>WEBCLIENT hitDto=/{}/, url=/{}/", hitDto, url);
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(hitDto);
        restTemplate.postForObject(url, request, EndpointHitDto.class);
    }

}
