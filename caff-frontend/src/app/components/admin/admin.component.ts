import { Component, OnInit } from '@angular/core';
import {UsersService} from "../../services/users.service";
import {User} from "../../models/user";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  public users: User[];

  constructor(private usersService: UsersService) { }

  ngOnInit(): void {
  this.usersService.getAllUsers().subscribe((result) => {
        this.users = result;
      })
  }

}
