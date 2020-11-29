import { Injectable } from '@angular/core';
import {Caff} from "../models/caff";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  public cart = new Map();

  constructor(private http: HttpClient) { }

  addCaffToCart(caff: Caff) {
    if(this.cart.has(caff)){
      this.cart.set(caff, this.cart.get(caff) + 1);
    } else {
      this.cart.set(caff, 1);
    }
  }

  removeCaffFromCart(caff: Caff) {
    this.cart.set(caff, this.cart.get(caff) - 1);
    if (this.cart.get(caff).value == 0)
      this.deleteCaffFromCart(caff);
  }

  deleteCaffFromCart(caff: Caff){
    this.cart.delete(caff);
  }

  clearCart(){
    this.cart = new Map();
  }

  downloadCaffs(){
    //TODO: get files from server
    this.clearCart();
  }
}
