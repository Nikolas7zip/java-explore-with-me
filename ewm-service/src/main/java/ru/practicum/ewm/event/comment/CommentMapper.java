package ru.practicum.ewm.event.comment;

import ru.practicum.ewm.event.comment.dto.CommentDto;
import ru.practicum.ewm.event.comment.dto.NewCommentDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static Comment mapToNewServiceComment(Long eventId, User author, NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setEventId(eventId);
        comment.setText(newCommentDto.getText());
        comment.setType(CommentType.SERVICE);

        return comment;
    }

    public static CommentDto mapToCommentDto(Comment comment, boolean isAdminAuthor) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setCreated(comment.getCreated());
        commentDto.setEventId(comment.getEventId());
        if (isAdminAuthor) {
            commentDto.setAuthor(new UserShortDto(null, "Admin"));
        } else {
            commentDto.setAuthor(new UserShortDto(
                    comment.getAuthor().getId(),
                    comment.getAuthor().getName())
            );
        }
        commentDto.setText(comment.getText());
        commentDto.setType(comment.getType());

        return commentDto;
    }

    public static List<CommentDto> mapToCommentDto(List<Comment> comments) {
        return comments.stream()
                .map(comment -> {
                    if (comment.getAuthor() == null) {
                        return mapToCommentDto(comment, true);
                    } else {
                        return mapToCommentDto(comment, false);
                    }
                }).collect(Collectors.toList());
    }
}
