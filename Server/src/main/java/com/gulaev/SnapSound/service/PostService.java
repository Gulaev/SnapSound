package com.gulaev.SnapSound.service;

import com.gulaev.SnapSound.dto.PostDTO;
import com.gulaev.SnapSound.entity.ImageModel;
import com.gulaev.SnapSound.entity.Post;
import com.gulaev.SnapSound.entity.User;
import com.gulaev.SnapSound.exception.PostNotFoundException;
import com.gulaev.SnapSound.repository.ImageRepository;
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
public class PostService {

  public static final Logger log = LoggerFactory.getLogger(PostService.class);
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final ImageRepository imageRepository;

  @Autowired
  public PostService(PostRepository postRepository, UserRepository userRepository,
      ImageRepository imageRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.imageRepository = imageRepository;
  }

  public Post create(PostDTO postDTO, Principal principal) {
    User user = getUserByPrincipal(principal);
    Post post = new Post();
    post.setUser(user);
    post.setCaption(postDTO.getCaption());
    post.setLocation(postDTO.getLocation());
    post.setTitle(postDTO.getTitle());
    post.setLikes(0);

    log.info("Saving Post for user  ", user.getEmail());
    return postRepository.save(post);
  }

  public List<Post> getAllPosts() {
    return postRepository.findAllByOrderByCreatedDateDesc();
  }

  public Post getPostById(Long id, Principal principal ) {
    User user = getUserByPrincipal(principal);
    return postRepository.findPostByIdAndUser(id, user).orElseThrow(
        () -> new PostNotFoundException("Post cannot by found for username: "+ user.getUsername())
    );
  }

  public List<Post> getAllPostByUser(Principal principal) {
    User user = getUserByPrincipal(principal);
    return postRepository.findAllByUserOrderByCreatedDateDesc(user);
  }

  public Post like(Long id, String username) {
    Post post = postRepository.findPostById(id).orElseThrow(
        () -> new PostNotFoundException("Post cannot by found for username: "+ username)
    );
    Optional<String> userLiked =  post.getLikeUsers()
            .stream()
                .filter(u -> u.equals(username)).findAny();
     if (userLiked.isPresent()) {
       post.setLikes(post.getLikes()- 1);
       post.getLikeUsers().remove(username);
     } else {
       post.setLikes(post.getLikes() + 1);
       post.getLikeUsers().add(username);
     }
     return postRepository.save(post);
  }

  private User getUserByPrincipal(Principal principal) {
    String username = principal.getName();
    return userRepository.findByUserName(username).orElseThrow(
        () -> new UsernameNotFoundException("Username not found " + username)
    );
  }

  public void deletePost(Long postId, Principal principal){
    Post post = getPostById(postId, principal);
    Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
    postRepository.delete(post);
    imageModel.ifPresent(imageRepository::delete);
  }
}
