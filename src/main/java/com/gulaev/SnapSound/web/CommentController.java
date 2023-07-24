package com.gulaev.SnapSound.web;

import com.gulaev.SnapSound.dto.CommentDTO;
import com.gulaev.SnapSound.entity.Comment;
import com.gulaev.SnapSound.facade.CommentFacade;
import com.gulaev.SnapSound.payload.response.MessageResponse;
import com.gulaev.SnapSound.service.CommentService;
import com.gulaev.SnapSound.service.ResponseErrorValidation;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController {

  @Autowired
  private CommentService commentService;
  @Autowired
  private CommentFacade commentFacade;
  @Autowired
  private ResponseErrorValidation responseErrorValidation;

  @PostMapping("/{postId}/create")
  public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDTO commentDTO,
      @PathVariable("postId") String postId, BindingResult bindingResult, Principal principal) {
    ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
    if(ObjectUtils.isEmpty(errors)) return errors;

    Comment comment = commentService. saveComment(Long.parseLong(postId), commentDTO, principal);
    CommentDTO createComment = commentFacade.commentToCommentDTO(comment);
    return new ResponseEntity<>(createComment, HttpStatus.OK);
  }

  @GetMapping("/{postId}/all")
  public ResponseEntity<Object> getAllCommentsToPost(@PathVariable("postId") String postId) {
    List<CommentDTO> commentDTOList = commentService.getAllCommentsForPost(Long.parseLong(postId))
        .stream()
        .map(commentFacade::commentToCommentDTO)
        .collect(Collectors.toList());
    return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
  }

  @PostMapping("/{commentId}/delete")
  public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
    commentService.deleteComment(Long.parseLong(commentId));
    return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
  }

}
