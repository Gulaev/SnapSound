package com.gulaev.SnapSound.repository;

import com.gulaev.SnapSound.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
   extends JpaRepository<User, Long> {

  Optional<User> findByUserName(String userName);
  Optional<User> findUserById(Long id);


}
