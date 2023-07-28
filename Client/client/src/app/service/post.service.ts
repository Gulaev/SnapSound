import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Post} from "../models/Post";

const USER_API = "http://localhost:8080/api/post/"

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private httpClient: HttpClient) { }

  createPost(post : Post) : Observable<any> {
    return this.httpClient.post(USER_API + 'create', post)
  }

  getAllPosts() : Observable<any> {
    return this.httpClient.get(USER_API + 'all')
  }

  getPostsForCurrentUser() : Observable<any> {
    return this.httpClient.get(USER_API + "user/posts")
  }

  delete(id : number) : Observable<any> {
    return this.httpClient.post(USER_API + id +'/delete', null)
  }

  likePost(id:number, username:string) : Observable<any> {
    return this.httpClient.post(USER_API + id + '/' + username + '/like', null);
  }




}
