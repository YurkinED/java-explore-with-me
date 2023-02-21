package ru.practicum.comments;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.EventRepository;
import ru.practicum.comments.model.CommentNewDto;
import ru.practicum.event.model.Event;
import ru.practicum.support.CommentNotFoundException;
import ru.practicum.support.UserNotFoundException;
import ru.practicum.users.UserRepository;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;


    @Override
    public CommentNewDto addCommentByEventId(Long eventId, Long userId, CommentNewDto commentNewDto) {

        User user = findUserById(userId);
        Event event = findEventById(eventId);

        Comment comment = commentMapper.commentNewDtoToComment(commentNewDto);

        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        comment.setEditedOn(null);
        Comment comment2 = commentRepository.save(comment);
        log.info("Comment={}", comment2);
        return commentMapper.commentToCommentNewDto(commentRepository.save(comment2));

    }

    @Override
    public List<CommentNewDto> findPublicEventByIdWithComments(Long eventId) {
        findEventById(eventId);
        return commentMapper.sourceListToTargetList(findAllCommentsEvent(eventId));
    }

    @Override
    public void deleteComment(Long eventId, Long commentId) {
        Event event = findEventById(eventId);
        CommentNewDto comment = findCommentById(eventId, commentId);

        if (!comment.getEventId().equals(eventId)) {
            throw new CommentNotFoundException(
                    String.format("Comment with id=%d to event with id-%d was not found", commentId, eventId));
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentNewDto updateEventComment(CommentNewDto commentNewDto, Long userId, Long eventId, Long commentId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);
        Comment comment = findCommentById2(eventId, commentId);
        log.info(" Long userId {},{}", userId, user);
        log.info(" Long eventId {},{}", eventId, event);
        log.info(" Long commentId {},{}", commentId, comment);


        if (!comment.getAuthor().equals(user) || !comment.getEvent().equals(event)) {
            throw new CommentNotFoundException(
                    String.format("Comment with id=%d to event with id-%d was not found", commentId, eventId));
        }

        comment.setText(commentNewDto.getText());
        comment.setEditedOn(LocalDateTime.now());

        return commentMapper.commentToCommentNewDto(commentRepository.save(comment));
    }

    private CommentNewDto findCommentById(Long eventId, Long commentId) {
        log.error("findCommentById {},{}", eventId, commentId);
        return commentMapper.commentToCommentNewDto(commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(
                        String.format("1_Comment with id=%d to event with id-%d was not found", commentId, eventId))));
    }

    private Comment findCommentById2(Long eventId, Long commentId) {
        log.error("findCommentById {},{}", eventId, commentId);
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(
                        String.format("2_Comment with id=%d to event with id-%d was not found", commentId, eventId)));
    }

    private List<Comment> findAllCommentsEvent(Long eventId) {
        return commentRepository.findAllCommentsEvent(eventId);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id=%d was not found", id)));
    }

    @Transactional(readOnly = true)
    public Event findEventById(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            return event.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event by this id not found");
        }
    }

}
