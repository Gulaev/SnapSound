import {Component, OnInit} from '@angular/core';
import {User} from "../../models/User";
import {TokenStorageService} from "../../service/token-storage.service";
import {UserService} from "../../service/user.service";
import {Route, Router} from "@angular/router";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit{

  isLoginIn = false
  isDataLoaded = false
  user! : User

  constructor(private tokenService: TokenStorageService,
              private userService: UserService,
              private router: Router) {}

  ngOnInit(): void {
    this.isLoginIn = !!this.tokenService.getToken()

    if (this.isLoginIn) {
      this.userService.getCurrentUser().subscribe(
        data => {
          this.user = data
          this.isDataLoaded = true
        }
      )
    }
  }

  logout() : void {
    this.tokenService.logOut()
    this.router.navigate(['/login'])
  }

}
