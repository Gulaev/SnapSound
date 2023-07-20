package com.gulaev.SnapSound.service;

import com.gulaev.SnapSound.entity.User;
import com.gulaev.SnapSound.entity.enums.ERole;
import com.gulaev.SnapSound.exception.UserExistException;
import com.gulaev.SnapSound.payload.request.SignupRequest;
import com.gulaev.SnapSound.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  public static final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  public User createUser(SignupRequest userIn) {
    User user = new User();
    user.setEmail(userIn.getEmail());
    user.setUserName(userIn.getUsername());
    user.setName(userIn.getFirstName());
    user.setLastName(userIn.getLastName());
    user.setPassword(bCryptPasswordEncoder.encode(userIn.getPassword()));
    user.getRole().add(ERole.ROLE_USER);

    try {
       log.info("Saving user");
        return userRepository.save(user);

    } catch (Exception e) {
      log.error("Error during registration {}", e.getMessage());
      throw new UserExistException("This user "+user.getUsername()
          + " already exist. Please check credentials");
    }
  }
}
