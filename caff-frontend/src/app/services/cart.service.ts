import { Injectable } from '@angular/core';
import {Caff} from "../models/caff";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  cart = new Map();

  constructor() { }

  addCaffToCart(caff: Caff) {
    if(this.cart.has(caff)){
      this.cart.set(caff, this.cart.get(caff) + 1);
    } else {
      this.cart.set(caff, 1);
    }
  }

  removeCaffFromCart(caff: Caff) {
    this.cart.set(caff, this.cart.get(caff).value--);
    if (this.cart.get(caff).value == 0)
      this.deleteCaffFromCart(caff);
  }

  deleteCaffFromCart(caff: Caff){
    this.cart.delete(caff);
  }
}
