package com.gulaev.SnapSound.web;

import com.gulaev.SnapSound.dto.PostDTO;
import com.gulaev.SnapSound.entity.Post;
import com.gulaev.SnapSound.facade.PostFacade;
import com.gulaev.SnapSound.payload.response.MessageResponse;
import com.gulaev.SnapSound.service.PostService;
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
@RequestMapping("api/post")
@CrossOrigin
public class PostController {

  @Autowired
  private PostFacade postFacade;
  @Autowired
  private PostService postService;
  @Autowired
  private ResponseErrorValidation responseErrorValidation;

  @PostMapping("/create")
  public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO,
      BindingResult bindingResult, Principal principal) {
    ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
    if (!ObjectUtils.isEmpty(errors)) return errors;

    Post post = postService.create(postDTO, principal);
    PostDTO createPost = postFacade.postToPostDTO(post);

    return new ResponseEntity<>(createPost, HttpStatus.OK);
  }

  @GetMapping("/all")
  public ResponseEntity<List<PostDTO>> getAllPost() {
    List<PostDTO> postDTOList = postService.getAllPosts()
        .stream()
        .map(postFacade::postToPostDTO)
        .collect(Collectors.toList());
    return new ResponseEntity<>(postDTOList, HttpStatus.OK);
  }


  @GetMapping("/user/posts")
  public ResponseEntity<List<PostDTO>> getAllPostForUser(Principal principal) {
    List<PostDTO> postDTOList = postService.getAllPostByUser(principal)
        .stream()
        .map(postFacade::postToPostDTO)
        .collect(Collectors.toList());
    return new ResponseEntity<>(postDTOList, HttpStatus.OK);
  }

  @PostMapping("/{postId}/{username}/like")
  public ResponseEntity<PostDTO> likePost(@PathVariable("postId") String postId,
      @PathVariable("username") String username) {
    Post post = postService.like(Long.parseLong(postId), username);
    PostDTO postDTO = postFacade.postToPostDTO(post);
    return new ResponseEntity<>(postDTO, HttpStatus.OK);
  }

  @PostMapping("/{postId}/delete")
  public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId,
      Principal principal) {
    postService.deletePost(Long.parseLong(postId), principal);
    return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
  }


}
