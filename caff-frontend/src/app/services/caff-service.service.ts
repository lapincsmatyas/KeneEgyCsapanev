import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Caff} from "../models/caff";
import {Observable} from "rxjs";
import {environment} from 'src/environments/environment';
import {Comment} from '../models/comment';
import {catchError, map} from "rxjs/operators";


@Injectable({
  providedIn: 'root'
})
export class CaffService implements OnInit{
  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
  }

  getAllCaffs(): Observable<Caff[]> {
    return this.http.get<Caff[]>(environment.apiUrl + '/caff');
  }

  getCaffById(id: number): Observable<Caff> {
    return this.http.get<Caff>(environment.apiUrl + `/caff/${id}`)
  }

  addComment(comment: Comment, id: number): Observable<Comment> {
    return this.http.put<Comment>(environment.authUrl + `/caff/${id}/comment`, comment);
  }

  uploadCaff(file: File): Observable<Caff> {
    const formData: FormData = new FormData();
    formData.append('fileKey', file, file.name);
    return this.http
      .post<Caff>(environment.authUrl + `/caff/upload`, formData)
  }

  getCaffPreview(id: number): Observable<Blob> {
    return this.http.get(environment.authUrl + `/caff/${id}/preview`, { responseType: 'blob' });
  }


}
