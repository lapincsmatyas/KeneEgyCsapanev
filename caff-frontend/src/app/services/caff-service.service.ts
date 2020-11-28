import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Caff} from "../models/caff";
import {Observable} from "rxjs";

const AUTH_API = 'http://localhost:3000';

@Injectable({
  providedIn: 'root'
})
export class CaffService {

  constructor(private http: HttpClient) { }

  getAllCaffs(): Observable<Caff[]>{
    return this.http.get<Caff[]>(`${AUTH_API}/caff`);
  }

  getCaffById(id: number): Observable<Caff>{
    return this.http.get<Caff>(`${AUTH_API}/caff/${id}`)
  }
}
