package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.model.CommentNewDto;
import ru.practicum.comments.model.Comment;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;


@Controller
@RequestMapping(path = "/comments/{userId}")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentControllerPrivate {

    private final CommentService commentService;


    @PostMapping("/events/{eventId}/comments")
    public ResponseEntity<CommentNewDto> addCommentByEventId(@PathVariable Long eventId, @PathVariable @Min(1) Long userId,
                                                       @Valid @RequestBody CommentNewDto comment) {

        log.info("addCommentByEventId {}",comment);
        return ResponseEntity.status(201).body(commentService.addCommentByEventId(eventId, userId, comment));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentNewDto> updateEventComment(@Valid @RequestBody CommentNewDto commentNewDto,
                                                      @Positive @PathVariable Long userId,
                                                      @Positive @PathVariable Long commentId
    ) {

        return ResponseEntity.ok(commentService.updateEventComment(commentNewDto, userId, commentNewDto.getEventId(), commentId));
    }
}
