package com.gulaev.SnapSound.web;

import com.gulaev.SnapSound.entity.ImageModel;
import com.gulaev.SnapSound.payload.response.MessageResponse;
import com.gulaev.SnapSound.service.ImageUploadService;
import java.io.IOException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/image")
@CrossOrigin
public class ImageUploadController {

  @Autowired
  private ImageUploadService imageUploadService;

  @PostMapping("/upload")
  public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
      Principal principal) throws IOException {
      imageUploadService.uploadImage(file, principal);
      return ResponseEntity.ok(new MessageResponse("Image upload Successfully"));
  }

  @PostMapping("/{postId}/upload")
  public ResponseEntity<MessageResponse> uploadImageToThePost(@PathVariable("postId") String postId,
      @RequestParam("file") MultipartFile file, Principal principal) throws IOException {
    imageUploadService.uploadImageToThePost(file, principal, Long.parseLong(postId));
    return ResponseEntity.ok(new MessageResponse("Image upload Successfully"));
  }

  @GetMapping("/profileImage")
  public ResponseEntity<ImageModel> getImageToUser(Principal principal) {
    ImageModel userImage = imageUploadService.getImageToUser(principal);
    return new ResponseEntity<>(userImage, HttpStatus.OK);
  }

  @GetMapping("/{postId}/image")
   public ResponseEntity<ImageModel> getImageToPost(@PathVariable("postId") String postId) {
    ImageModel imageModel = imageUploadService.getImagePost(Long.parseLong(postId));
    return new ResponseEntity<>(imageModel, HttpStatus.OK);
  }
}
