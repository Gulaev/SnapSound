package com.gulaev.SnapSound.facade;

import com.gulaev.SnapSound.dto.CommentDTO;
import com.gulaev.SnapSound.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {

  public CommentDTO commentToCommentDTO(Comment comment) {
    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setId(comment.getId());
    commentDTO.setUsername(comment.getUserName());
    commentDTO.setMessage(comment.getMessage());

    return commentDTO;
  }

}
