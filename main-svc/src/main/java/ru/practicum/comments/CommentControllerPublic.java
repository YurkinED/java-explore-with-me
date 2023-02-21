package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comments.model.CommentNewDto;

import java.util.List;

@RestController
@RequestMapping(path = "/comment")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentControllerPublic {

    private final CommentService commentService;


    @GetMapping("/{eventId}")

    public ResponseEntity<List<CommentNewDto>> findEventByIdWithComments(@PathVariable Long eventId) {
        return ResponseEntity.ok(commentService.findPublicEventByIdWithComments(eventId));
    }

}
