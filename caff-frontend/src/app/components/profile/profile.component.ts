import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {AuthService} from "../../services/auth.service";
import {User} from "../../models/user";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: User;

  userName: string;
  email: string;

  edit = false;

  constructor(public userService: UserService,
              private authService: AuthService
  ) {
  }

  ngOnInit(): void {
    this.userService.getUserData(this.authService.getCurrentUser().username).subscribe(result => {
      this.user = result;
      this.userName = this.user.username;
      this.email = this.user.email;
    });
  }

  toggleEdit() {
    this.edit = !this.edit;
  }

  saveData(){
    if(this.userName != this.user.username){
      if(confirm("Felhasználónév változtatásakor újra be kell jelentkezned. Biztosan megváltoztatod a felhasználóneved?")) {
        this.user.username = this.userName;
        this.user.email = this.email;
        this.userService.changeUserData(this.user).subscribe(result =>{
          this.authService.logout();
        })
      }
    }
  }
}
