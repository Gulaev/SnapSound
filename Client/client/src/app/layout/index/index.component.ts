import {Component, OnInit} from '@angular/core';
import {Post} from "../../models/Post";
import {User} from "../../models/User";
import {PostService} from "../../service/post.service";
import {UserService} from "../../service/user.service";
import {CommentService} from "../../service/comment.service";
import {NotificationService} from "../../service/notification.service";
import {ImageUploadService} from "../../service/image-upload.service";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit{

  isPostIsLoad = false
  posts!: Post[]
  isUserDataLoad = false
  user! : User

  constructor(private postService: PostService,
              private userService: UserService,
              private commentService: CommentService,
              private notificationService: NotificationService,
              private imageService: ImageUploadService) {
  }

  ngOnInit(): void {
    this.postService.getAllPosts()
    .subscribe(data =>{
      console.log(data)
      this.posts = data
      this.isPostIsLoad = true
    })
    this.userService.getCurrentUser()
        .subscribe(data => {
        this.user = data
        this.isUserDataLoad = true
    })
  }

  getImageToPost(posts: Post[]) : void{
    posts.forEach(p => {
      if (p.id != null) {
        this.imageService.getImageToPost(p.id)
          .subscribe(data => {
            p.image = data.imageBytes
          })
      }
    })
  }

}
