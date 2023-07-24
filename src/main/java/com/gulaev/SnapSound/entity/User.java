package com.gulaev.SnapSound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gulaev.SnapSound.entity.enums.ERole;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(nullable = false )
  private String name;

  @Column(nullable = false, updatable = true)
  private String userName;

  @Column(nullable = false)
  private String lastName;

  @Column(unique = true)
  private String email;

  @Column(columnDefinition = "text")
  private String bio;

  @Column(length = 3000)
  private String password;

  @ElementCollection(targetClass = ERole.class)
  @CollectionTable(name = "users_role",
      joinColumns = @JoinColumn(name = "users_id"))
  private Set<ERole> role  = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user",
      orphanRemoval = true)
  private List<Post> posts = new ArrayList<>();

  @JsonFormat(pattern = "yyy-mmm-dd HH:mm:ss")
  @Column(updatable = false)
  private LocalDateTime createDate;

  @Transient
  private Collection<?extends GrantedAuthority> authorities;

  public User() {

  }

  @PrePersist
  protected void onCreate() {
    this.createDate = LocalDateTime.now();
  }

  public User(Long id, String userName, String email, String password,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.userName = userName;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  /**
   * Security
   */

  @Override
  public String getUsername() {
    return userName;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
