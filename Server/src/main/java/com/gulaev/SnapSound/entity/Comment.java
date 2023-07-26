package com.gulaev.SnapSound.entity;


import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import lombok.Data;

@Data
@Entity
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.EAGER)
  private Post post;
  @Column(nullable = false)
  private String userName;
  @Column(nullable = false)
  private Long userId;
  @Column(columnDefinition = "text", nullable = false)
  private String message;
  @Column(updatable = false)
  private LocalDateTime createdDate;


  @PrePersist
  protected void onCreated() {
    this.createdDate = LocalDateTime.now();
  }

}
