import {Component, OnInit} from '@angular/core';
import {User} from "../../models/user";
import {UserService} from "../../services/user.service";
import {ToastrService} from "ngx-toastr";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  public users: User[];

  constructor(private userService: UserService, private toastr: ToastrService, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe((result) => {
      this.users = result;
    }, error => {
      this.toastr.error("You are not permitted to this operation!");
    })
  }

  isAdmin(user: User) {
    return user.strRoles.includes("admin");
  }

  notMe(user: User) {
    return this.authService.getCurrentUser().user_id != user.user_id;
  }

  makeAdmin(user: User){
    return this.userService.makeAdmin(user.user_id).subscribe( result => {
      this.ngOnInit();
    })
  }

  revokeAdmin(user: User) {
    return this.userService.revokeAdmin(user.user_id).subscribe( result => {
      this.ngOnInit();
    })
  }
}
