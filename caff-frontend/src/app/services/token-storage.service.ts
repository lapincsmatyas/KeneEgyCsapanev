import { Injectable } from '@angular/core';
import {User} from "../models/user";

const TOKEN_KEY = 'jwt_key';
const USER_KEY = 'user_key';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  user: User = new User();

  private token = new Map();

  constructor() { }

  public saveToken(token: string): void {
    //window.sessionStorage.removeItem(TOKEN_KEY);
    //window.sessionStorage.setItem(TOKEN_KEY, token);
    this.token.delete(TOKEN_KEY);
    this.token.set(TOKEN_KEY, token);

  }

  public getToken(): string {
    return this.token.get(TOKEN_KEY);
    //return sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user: User): void {
    //window.sessionStorage.removeItem(USER_KEY);
    //window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));

    this.token.delete(USER_KEY);
    this.token.set(USER_KEY, JSON.stringify(user));
  }


  public getUser(): User {
    if(this.token.has(USER_KEY)) {
      this.user =  JSON.parse(this.token.get(USER_KEY));
      return this.user;
    }
    return null;
  }

  logout() {
    //window.sessionStorage.removeItem(USER_KEY);
    //window.sessionStorage.removeItem(TOKEN_KEY);
    this.token.delete(USER_KEY);
    this.token.delete(TOKEN_KEY);
  }
}
