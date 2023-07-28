import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const COMMENT_APi = 'http://localhost:8080/api/image/'

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient) { }

  addComment(commentId:number, message:string): Observable<any> {
    return this.http.post(COMMENT_APi + commentId + '/create', {message:message})
  }

  getCommentToPost(commentId:number) : Observable<any> {
    return this.http.get(COMMENT_APi + commentId + '/all')
  }

  delete(commentId:number) : Observable<any> {
    return this.http.get(COMMENT_APi + commentId + '/delete', {})
  }
}
