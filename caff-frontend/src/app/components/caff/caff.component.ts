import {Component, Input, OnInit} from '@angular/core';
import {CaffService} from "../../services/caff-service.service";
import {ActivatedRoute} from "@angular/router";
import {Caff} from "../../models/caff";
import {CartService} from "../../services/cart.service";

@Component({
  selector: 'app-caff',
  templateUrl: './caff.component.html',
  styleUrls: ['./caff.component.css']
})
export class CaffComponent implements OnInit {
  @Input()
  id: number;

  caff: Caff;

  constructor(private caffService: CaffService,
              private cartService: CartService,
              private route: ActivatedRoute
  ) {

  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.caffService.getCaffById(+params['id']).subscribe( result => {
        this.caff = result;
      })
    });
  }

  addCaffToCart() {
    this.cartService.addCaffToCart(this.caff);
    console.log(this.cartService.cart);
  }
}
