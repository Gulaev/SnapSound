package com.gulaev.SnapSound.service;

import com.gulaev.SnapSound.entity.ImageModel;
import com.gulaev.SnapSound.entity.Post;
import com.gulaev.SnapSound.entity.User;
import com.gulaev.SnapSound.exception.ImageNotFoundException;
import com.gulaev.SnapSound.repository.ImageRepository;
import com.gulaev.SnapSound.repository.UserRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUploadService {

  public static final Logger log = LoggerFactory.getLogger(ImageUploadService.class);
  private final ImageRepository imageRepository;
  private final UserRepository userRepository;
  private final PostService postService;

  @Autowired
  public ImageUploadService(ImageRepository imageRepository, UserRepository userRepository,
      PostService postService) {
    this.imageRepository = imageRepository;
    this.userRepository = userRepository;
    this.postService = postService;
  }

  public ImageModel uploadImage(MultipartFile multipartFile, Principal principal)
      throws IOException {
    User user = getUserByPrincipal(principal);
    log.info("Upload image profile to User {}", user.getUsername());
    ImageModel userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
    if (!ObjectUtils.isEmpty(userProfileImage)) {
      imageRepository.delete(userProfileImage);
    }
    ImageModel imageModel = new ImageModel();
    imageModel.setUserId(user.getId());
    imageModel.setImageByte(compressBytes(multipartFile.getBytes()));
    imageModel.setName(multipartFile.getOriginalFilename());
    return imageRepository.save(imageModel);
  }


  public ImageModel uploadImageToThePost(MultipartFile file, Principal principal, Long postId) throws IOException {
    User user = getUserByPrincipal(principal);
    Post post = user.getPosts().stream()
        .filter(p-> p.getId().equals(postId))
        .collect(toSingleCollection());
    ImageModel imageModel = new ImageModel();
    imageModel.setUserId(user.getId());
    imageModel.setImageByte(compressBytes(file.getBytes()));
    imageModel.setName(file.getOriginalFilename());
    log.info("Upload image for Post {}", postId);
    return imageRepository.save(imageModel);
  }

  public ImageModel getImageToUser(Principal principal) {
    User user = getUserByPrincipal(principal);
    ImageModel imageModel = imageRepository.findByUserId(user.getId()).orElse(null);
    if(!ObjectUtils.isEmpty(imageModel)) {
      imageModel.setImageByte(decompressBytes(imageModel.getImageByte()));
    }
    return imageModel;
  }

  public ImageModel getImagePost(Long postId) {
    ImageModel imageModel= imageRepository.findByPostId(postId)
        .orElseThrow(()-> new ImageNotFoundException("Cannot find image to Post: " + postId));
    if (!ObjectUtils.isEmpty(imageModel)) {
      imageModel.setImageByte(decompressBytes(imageModel.getImageByte()));
    }
    return imageModel;
  }

  private User getUserByPrincipal(Principal principal) {
    String username = principal.getName();
    return userRepository.findByUserName(username).orElseThrow(
        () -> new UsernameNotFoundException("Username not found " + username)
    );
  }

  private byte[] compressBytes(byte[] data) {
    Deflater deflater = new Deflater();
    deflater.setInput(data);
    deflater.finish();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] buffer = new byte[1024];
    while (!deflater.finished()) {
      int count = deflater.deflate(buffer);
      outputStream.write(buffer, 0, count);
    }
    try {
      outputStream.close();
    } catch (IOException e) {
      log.error("Cannot compress Bytes");
    }
    System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
    return outputStream.toByteArray();
  }

  private static byte[] decompressBytes(byte[] data) {
    Inflater inflater = new Inflater();
    inflater.setInput(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] buffer = new byte[1024];
    try {
      while (!inflater.finished()) {
        int count = inflater.inflate(buffer);
        outputStream.write(buffer, 0, count);
      }
      outputStream.close();
    } catch (IOException | DataFormatException e) {
      log.error("Cannot decompress Bytes");
    }
    return outputStream.toByteArray();
  }


  private <T> Collector<T,?,T>toSingleCollection() {
    return Collectors.collectingAndThen(
        Collectors.toList(),
        list -> {
          if (list.size() != 1) {
            throw new IllegalStateException();
          }
        return list.get(0);
        }
    );
  }
}
