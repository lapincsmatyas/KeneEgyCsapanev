import {Component, Input, OnInit} from '@angular/core';
import {CaffService} from "../../services/caff-service.service";
import {ActivatedRoute} from "@angular/router";
import {Caff} from "../../models/caff";
import {CartService} from "../../services/cart.service";
import {Comment} from 'src/app/models/comment';
import {AuthService} from 'src/app/services/auth.service';

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
              private authService: AuthService
  ) {

  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.caffService.getCaffById(+params['id']).subscribe(result => {
        this.caff = result;
      })
    });
  }

  addCaffToCart() {
    this.cartService.addCaffToCart(this.caff);
    console.log(this.cartService.cart);
  }

  addComment() {
    this.comment.userName = this.authService.getCurrentUser().username;
    console.log(this.comment);
    this.caffService.addComment(this.comment, this.caff.id).subscribe((comment: Comment) => {
      this.caff.comments.push(comment);
    });
    this.comment.comment = "";
  }

}
