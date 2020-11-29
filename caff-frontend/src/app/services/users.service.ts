import { Injectable } from '@angular/core';
import {HttpClient } from "@angular/common/http";
import {User} from "../models/user";
import {Observable} from "rxjs";
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<User[]>{
      return this.http.get<User[]>(environment.authUrl + '/admin/user');
    }
}
