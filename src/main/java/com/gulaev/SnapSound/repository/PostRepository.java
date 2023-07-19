package com.gulaev.SnapSound.repository;

import com.gulaev.SnapSound.entity.Post;
import com.gulaev.SnapSound.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  List<Post> findAllByUserOrderByCreatedDateDesc(User user);
  List<Post> findAllByOrderByCreatedDateDesc();
  Optional<Post> findPostByIdAndUser(Long id, User user);
}
