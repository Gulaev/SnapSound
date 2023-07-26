package com.gulaev.SnapSound.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;
import org.hibernate.annotations.Type;

@Data
@Entity
public class ImageModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Type(type="org.hibernate.type.BinaryType")
  @Column(name = "image_byte")
  private byte[] imageByte;
  @JsonIgnore
  private Long userId;
  @JsonIgnore
  private Long postId;

}
