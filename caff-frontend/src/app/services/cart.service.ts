import { Injectable } from '@angular/core';
import {Caff} from "../models/caff";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  public cart = new Set();

  constructor(private http: HttpClient) { }

  addCaffToCart(caff: Caff) {
    if(!this.cart.has(caff)){
      this.cart.add(caff);
    }
  }


  deleteCaffFromCart(caff: Caff){
    this.cart.delete(caff);
  }

  clearCart(){
    this.cart = new Set();
  }

  downloadCaffs(){
    //TODO: get files from server
    this.clearCart();
  }
}
