import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../service/auth.service";
import {TokenStorageService} from "../../service/token-storage.service";
import {NotificationService} from "../../service/notification.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit{

  public formGroup!: FormGroup

  constructor(private authService:AuthService,
              private tokenStorage: TokenStorageService,
              private notification: NotificationService,
              private router: Router,
              private fb: FormBuilder) {
    if (tokenStorage.getUser()) {
      this.router.navigate(['/'])
    }
  }

  ngOnInit(): void {
    this.formGroup = this.createFormRegister()
  }

  createFormRegister() : FormGroup {
    return this.fb.group({
      email: ['', Validators.compose([Validators.required, Validators.email])],
      username: ['', Validators.compose([Validators.required])],
      firstName: ['', Validators.compose([Validators.required])],
      lastName: ['', Validators.compose([Validators.required])],
      password: ['', Validators.compose([Validators.required])],
      confirmPassword: ['', Validators.compose([Validators.required])]
    })
  }

  submitRegister() : void {
    console.log(this.formGroup.value);
    this.authService.register({
      email: this.formGroup.value.email,
      username: this.formGroup.value.username,
      firstName: this.formGroup.value.firstName,
      lastName: this.formGroup.value.lastName,
      password: this.formGroup.value.password,
      confirmPassword: this.formGroup.value.confirmPassword
    }).subscribe(data => {
    console.log(data);
      this.notification.showSnackBar("Successfully registration")
    }, error => {
      this.notification.showSnackBar(error.value)
      }
    )
  }
}
