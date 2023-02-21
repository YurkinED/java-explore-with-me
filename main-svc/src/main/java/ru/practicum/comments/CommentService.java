package ru.practicum.comments;

import ru.practicum.comments.model.CommentNewDto;

import java.util.List;

public interface CommentService {
    CommentNewDto addCommentByEventId(Long eventId, Long userId, CommentNewDto comment);

    List<CommentNewDto> findPublicEventByIdWithComments(Long eventId);

    void deleteComment(Long eventId, Long commentId);

    CommentNewDto updateEventComment(CommentNewDto commentNewDto, Long userId, Long eventId, Long commentId);
}
