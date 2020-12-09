import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import {TokenStorageService} from "./token-storage.service";
import {User} from "../models/user";
import {Router} from "@angular/router";
import {map} from "rxjs/operators";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
    providedIn: 'root'
  })
  export class AuthService {

    constructor(
      private http: HttpClient,
      private router: Router,
      private tokenStorage: TokenStorageService
    ) { }

    login(credentials): Observable<any> {
      return this.http.post<User>(environment.authUrl + '/auth/signin', {
        username: credentials.username,
        password: credentials.password
      }, httpOptions).pipe( map (user => {
        this.tokenStorage.saveToken(user.jwt);
        this.tokenStorage.saveUser(user);
        })
      );
    }

    logout() {
      this.tokenStorage.logout();
      this.router.navigateByUrl('/login');
    }

    register(user): Observable<any> {
      return this.http.post(environment.authUrl + '/auth/signup', {
        username: user.username,
        email: user.email,
        password: user.password,
      }, httpOptions);
    }

    isLoggedIn(): boolean {
      return this.getCurrentUser() != null;
    }

    getCurrentUser(): User {
      return this.tokenStorage.getUser();
    }

    isAdmin(): boolean {
      return this.getCurrentUser()?.roles.includes("ROLE_ADMIN");
    }
  }
