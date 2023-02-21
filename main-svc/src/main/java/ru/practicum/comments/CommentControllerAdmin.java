package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentControllerAdmin {
    private final CommentService commentService;

    @DeleteMapping("/{eventId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long eventId, @PathVariable Long commentId) {
        commentService.deleteComment(eventId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
