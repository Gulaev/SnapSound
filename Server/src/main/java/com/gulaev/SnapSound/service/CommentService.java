package com.gulaev.SnapSound.service;

import com.gulaev.SnapSound.dto.CommentDTO;
import com.gulaev.SnapSound.entity.Comment;
import com.gulaev.SnapSound.entity.Post;
import com.gulaev.SnapSound.entity.User;
import com.gulaev.SnapSound.exception.PostNotFoundException;
import com.gulaev.SnapSound.repository.CommentRepository;
import com.gulaev.SnapSound.repository.PostRepository;
import com.gulaev.SnapSound.repository.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
  public static final Logger log = LoggerFactory.getLogger(CommentService.class);

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Autowired
  public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal) {
    User user = getUserByPrincipal(principal);
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + user.getEmail()));

    Comment comment = new Comment();
    comment.setPost(post);
    comment.setUserId(user.getId());
    comment.setUserName(user.getUsername());
    comment.setMessage(commentDTO.getMessage());

    log.info("Saving comment for Post: {}", post.getId());
    return commentRepository.save(comment);
  }

  public List<Comment> getAllCommentsForPost(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
    List<Comment> comments = commentRepository.findAllByPost(post);

    return comments;
  }

  public void deleteComment(Long commentId) {
    Optional<Comment> comment = commentRepository.findById(commentId);
    comment.ifPresent(commentRepository::delete);
  }


  private User getUserByPrincipal(Principal principal) {
    String username = principal.getName();
    return userRepository.findByUserName(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
  }
}
