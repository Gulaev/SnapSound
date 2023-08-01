import {Component, NgModule, OnInit} from '@angular/core';
import {AuthService} from "../../service/auth.service";
import {TokenStorageService} from "../../service/token-storage.service";
import {NotificationService} from "../../service/notification.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validator, Validators} from "@angular/forms";
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from "@angular/material/form-field";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  public loginForm!: FormGroup

  constructor(
    private authService: AuthService,
    private tokenStorage: TokenStorageService,
    private notification: NotificationService,
    private router: Router,
    private fb: FormBuilder
  ) {
    if (tokenStorage.getUser()) {
      this.router.navigate(['main'])
    }
  }

  ngOnInit() : void {
    this.loginForm = this.createLoginForm()
  }

  createLoginForm() : FormGroup {
    return this.fb.group({
      username: ['', Validators.compose([Validators.required])],
      password: ['', Validators.compose([Validators.required])]
    })
  }

  submit() : void {
    this.authService.login({
      username: this.loginForm.value.username,
      password: this.loginForm.value.password
      }).subscribe(
        data => {
          console.log(data)
          this.tokenStorage.saveToken(data.token)
          this.tokenStorage.saveUser(data)
          this.notification.showSnackBar('Successfully logged in')
          this.router.navigate(['/'])
          window.location.reload()
        }, error => {
          console.log(error)
        this.notification.showSnackBar(error.getMessage())
      }
    )
  }
}

