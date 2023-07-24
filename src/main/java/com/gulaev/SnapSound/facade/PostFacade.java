package com.gulaev.SnapSound.facade;

import com.gulaev.SnapSound.dto.PostDTO;
import com.gulaev.SnapSound.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

  public PostDTO postToPostDTO(Post post) {
    PostDTO postDTO = new PostDTO();
    postDTO.setId(post.getId());
    postDTO.setTitle(post.getTitle());
    postDTO.setCaption(post.getCaption());
    postDTO.setUsername(post.getUser().getUsername());
    postDTO.setLocation(post.getLocation());
    postDTO.setLikes(post.getLikes());
    postDTO.setUserLikes(post.getLikeUsers());

    return postDTO;
  }

}
