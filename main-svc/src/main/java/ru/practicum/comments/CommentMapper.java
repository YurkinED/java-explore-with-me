package ru.practicum.comments;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentNewDto;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface CommentMapper {

    List<CommentNewDto> sourceListToTargetList(List<Comment> sourceList);


    Comment commentNewDtoToComment(CommentNewDto commentNewDto);

    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "created", target = "created")
    CommentNewDto commentToCommentNewDto(Comment commentNewDto);


}
