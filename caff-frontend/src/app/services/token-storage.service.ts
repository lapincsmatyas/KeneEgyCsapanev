import { Injectable } from '@angular/core';
import {User} from "../models/user";

const TOKEN_KEY = 'jwt_key';
const USER_KEY = 'user_key';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  user: User = new User();

  constructor() { }

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user: User): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  
  public getUser(): User {
    this.user = JSON.parse(sessionStorage.getItem(USER_KEY));
    console.log("user: " + this.user);
    return this.user;

  }

  logout() {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.removeItem(TOKEN_KEY);
  }
}
