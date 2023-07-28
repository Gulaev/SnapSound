import { Injectable } from '@angular/core';
import {User} from "../models/User";

const TOKEN_KEY = 'auth_token'
const USER_KEY = 'auth_user '

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  constructor() { }

  public saveToken(token:string) :void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token)
  }

  public getToken() : string | any {
     return sessionStorage.getItem(TOKEN_KEY)
  }

  public saveUser(user : any) : void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): any {
    const userStr = sessionStorage.getItem(USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
  }
  logOut() : void {
    window.sessionStorage.clear()
    window.location.reload()
  }
}
