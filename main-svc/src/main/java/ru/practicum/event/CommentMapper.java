package ru.practicum.event;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import ru.practicum.event.dto.CommentDto;
import ru.practicum.event.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface CommentMapper {

    @Mapping(source = "author.name", target = "authorName")
    CommentDto commentToCommentDto(Comment comment);

    Comment commentDtoToComment(CommentDto commentDto);

    List<CommentDto> sourceListToTargetList(List<Comment> sourceList);

    @Mapping(target = "id", ignore = true)
    Comment updateCommentFromDto(CommentDto commentDto, @MappingTarget Comment comment);

}
