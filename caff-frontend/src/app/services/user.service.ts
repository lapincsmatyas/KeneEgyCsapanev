import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../models/user";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUserData(username: string): Observable<User>{
    return this.http.get<User>(`${environment.apiUrl}/user/${username}`);
  }

  getAllUsers(): Observable<User[]>{
    return this.http.get<User[]>(`${environment.apiUrl}/admin/user`);
  }

  makeAdmin(id: number) {
    return this.http.get<User>(`${environment.apiUrl}/admin/user/${id}/admin`);
  }

  revokeAdmin(id: number) {
    return this.http.delete<User>(`${environment.apiUrl}/admin/user/${id}/admin`);
  }
}
