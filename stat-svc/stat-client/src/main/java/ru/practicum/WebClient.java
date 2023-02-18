package ru.practicum;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WebClient {


    private final RestTemplate restTemplate;
    private final String statUrl;


    public WebClient(RestTemplate restTemplate, @Value("${statistic-server.uri}") String statUrl) {
        this.restTemplate = restTemplate;
        this.statUrl = statUrl;
    }

    public void addToStatistic(String app, HttpServletRequest httpServletRequest) {
        EndpointHitDto hitDto = new EndpointHitDto();
        hitDto.setApp(app);
        hitDto.setIp(httpServletRequest.getRemoteAddr());
        hitDto.setUri(httpServletRequest.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now());
        String url = statUrl + "/hit";
        log.info("---===>>>WEBCLIENT hitDto=/{}/, url=/{}/", hitDto, url);
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(hitDto);
        restTemplate.postForObject(url, request, EndpointHitDto.class);
    }

    public ResponseEntity<Object> get(String url, Map<String, Object> parameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class, parameters);
        log.info("statUrl +url{}", url);
        log.info("Response={}", response);
        log.info("getBody={}", response.getBody());
        return response;
    }

    public List<ViewStats> getViewsAll(Set<Long> eventsId) {
        String url = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";

        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "end", LocalDateTime.now().plusYears(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "uris", (eventsId.stream().map(id -> "/events/" + id).collect(Collectors.toList())),
                "unique", "false"
        );
        log.info(eventsId.stream().map(id -> "/events/" + id).collect(Collectors.toList()).toString());
        ResponseEntity<Object> response = get(statUrl + url, parameters);
        return response.hasBody() ? (List<ViewStats>) response.getBody() : List.of();
    }

    public List<ViewStats> getViewsAll2(Set<Long> eventsId) {
        String url = "/stats?start={start}&end={end}&unique={unique}";

        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "end", LocalDateTime.now().plusYears(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "unique", "false"
        );
        log.info(eventsId.stream().map(id -> "/events/" + id).collect(Collectors.toList()).toString());
        ResponseEntity<Object> response = get(statUrl + url, parameters);
        log.info("response.getBody()={}",response.getBody());
        log.info("response.getBody()toString={}",response.getBody().toString());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        List<ViewStats>  viewStats = Arrays.asList(mapper.convertValue(response.getBody(), ViewStats[].class));
        log.info(" List<ViewStats>  viewStats ={}",viewStats);
        return response.hasBody() ? viewStats : List.of();
    }

}