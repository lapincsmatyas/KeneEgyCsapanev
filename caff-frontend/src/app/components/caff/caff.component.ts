import {Component, Input, OnInit} from '@angular/core';
import {CaffService} from "../../services/caff-service.service";
import {ActivatedRoute} from "@angular/router";
import {Caff} from "../../models/caff";
import {CartService} from "../../services/cart.service";
import { Comment } from 'src/app/models/comment';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-caff',
  templateUrl: './caff.component.html',
  styleUrls: ['./caff.component.css']
})
export class CaffComponent implements OnInit {
  @Input()
  id: number;
  caff: Caff;
  comment: Comment = new Comment();


  constructor(private caffService: CaffService,
              private cartService: CartService,
              private route: ActivatedRoute,
              private tokenStorage: TokenStorageService
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

  addComment(){
    this.comment.username = this.tokenStorage.getUser.name;
    console.log(this.comment.comment);
    this.caffService.addComment(this.comment, this.caff.id).subscribe(() => {
      this.caffService.getCaffById(this.caff.id).subscribe(
      data => {
      this.caff = data;
          });
    });
    this.comment.comment = "";
  }

}
