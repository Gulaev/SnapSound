package com.gulaev.SnapSound.facade;

import com.gulaev.SnapSound.dto.UserDTO;
import com.gulaev.SnapSound.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

  public UserDTO userToUserDTO(User user) {
    UserDTO userDTO = new UserDTO();
    userDTO.setBio(user.getBio());
    userDTO.setUsername(user.getUsername());
    userDTO.setFirstName(user.getName());
    userDTO.setLastName(user.getLastName());
    userDTO.setId(user.getId());
    return userDTO;
  }

}
