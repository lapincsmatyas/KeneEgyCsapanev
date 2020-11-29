import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Caff} from "../models/caff";
import {Observable} from "rxjs";
import { environment } from 'src/environments/environment';
import { Comment } from '../models/comment';



@Injectable({
  providedIn: 'root'
})
export class CaffService {

  constructor(private http: HttpClient) { }

  getAllCaffs(): Observable<Caff[]>{
    return this.http.get<Caff[]>(environment.apiUrl + '/caff');
  }

  getCaffById(id: number): Observable<Caff>{
    return this.http.get<Caff>(environment.apiUrl + `/caff/${id}`)
  }

  addComment(comment: Comment, id: number): Observable<any> {
    return this.http.put<any>(environment.authUrl + `/${id}/comment`, { comment });
  }

}
