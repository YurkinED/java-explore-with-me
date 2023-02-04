package ru.practicum.stat;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatisticDtoIn;
import ru.practicum.stat.model.StatData;
import ru.practicum.dto.StatisticDto;

import java.util.Collection;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatDataController {
    private final StatDataService statDataService;

    @GetMapping("/stats")                                    // метод получения пользователя по Id
    public ResponseEntity<Collection<StatisticDto>> getStats(@NonNull @RequestParam String start,
                                                             @NonNull @RequestParam String end,
                                                             @NonNull @RequestParam String[] uris,
                                                             @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Get stats with start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return new ResponseEntity<>(statDataService.getStats(start, end, uris, unique), HttpStatus.OK);
    }

    @PostMapping("/hit")                         // метод добавления пользователя
    public ResponseEntity<StatData> postStat(@RequestBody StatisticDtoIn statistic) {
        log.info("Creating stat record {}", statistic);
        return new ResponseEntity<>(statDataService.postStat(statistic), HttpStatus.OK);
    }
}
