package com.gulaev.SnapSound.repository;

import com.gulaev.SnapSound.entity.Comment;
import com.gulaev.SnapSound.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findAllByPost(Post post);

  Optional<Comment> findByIdAndUserId(Long commentId, Long userId);

}
