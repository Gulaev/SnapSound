package com.gulaev.SnapSound.service;

import com.gulaev.SnapSound.entity.User;
import com.gulaev.SnapSound.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUserName(username);
    if (user.isPresent()) {
      return build(user.get());
    } else {
      throw new UsernameNotFoundException("User not found with this username "+ username);
    }
  }


  public UserDetails loadUserById(Long id) {
    Optional<User> user = userRepository.findUserById(id);
    if (user.isPresent()) {
      return build(user.get());
    } else {
      throw new UsernameNotFoundException("User not found with this id "+ id);
    }
  }

  public static User build(User user) {
    List<GrantedAuthority> authorities = user.getRole().stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toList());

    return new User(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getPassword(),
        authorities);
  }
}
