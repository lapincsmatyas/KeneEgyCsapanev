import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
    providedIn: 'root'
  })
  export class AuthService {

    constructor(private http: HttpClient) { }

    login(credentials): Observable<any> {
      return this.http.post(environment.authUrl + '/auth/signin', {
        username: credentials.username,
        password: credentials.password
      }, httpOptions);
    }

    register(user): Observable<any> {
      return this.http.post(environment.authUrl + '/auth/signup', {
        username: user.username,
        email: user.email,
        password: user.password,
      }, httpOptions);
    }
  }
