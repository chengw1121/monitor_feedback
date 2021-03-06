import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {UserService} from '../shared/services/user.service';
import {ApiUser} from '../shared/models/api_user';


@Component({
  moduleId: module.id,
  selector: 'sd-login',
  templateUrl: 'login.component.html',
  styleUrls: ['login.component.css'],
})
export class LoginComponent {
  errorMessage:string;

  constructor(private userService:UserService, private router:Router) {
  }

  formSubmitted(event, username, password) {
    event.preventDefault();
    this.userService.loginOnRepository(username, password).subscribe(
      (result) => {
        if (result) {
          this.storeApiUserIdForName(username);
        }
      },
      error => this.errorMessage = 'Username or password is wrong'
    );
  }

  storeApiUserIdForName(username:string) {
    this.userService.getUsers().subscribe(
      (result) => {
        let user:ApiUser = result.filter(apiUser => apiUser.name === username)[0];
        localStorage.setItem('api_user_id', String(user.id));
        this.router.navigate(['']);
      },
      error => {
        console.log(error);
      }
    )
  }
}
