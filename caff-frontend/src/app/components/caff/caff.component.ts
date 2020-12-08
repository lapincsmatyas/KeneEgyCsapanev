import {Component, Input, OnInit} from '@angular/core';
import {CaffService} from "../../services/caff-service.service";
import {ActivatedRoute} from "@angular/router";
import {Caff} from "../../models/caff";
import {CartService} from "../../services/cart.service";
import {Comment} from 'src/app/models/comment';
import {AuthService} from 'src/app/services/auth.service';
import {DomSanitizer} from "@angular/platform-browser";
import {saveAs} from "file-saver";
import {ToastrService} from "ngx-toastr";


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
              private authService: AuthService,
              private sanitizer: DomSanitizer,
              private toastr: ToastrService
  ) {

  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.caffService.getCaffById(+params['id']).subscribe(caff => {
        this.caff = caff;
        this.caffService.getCaffPreview(caff.id).subscribe((preview) => {
          let objectURL = URL.createObjectURL(preview);
          caff.preview = this.sanitizer.bypassSecurityTrustUrl(objectURL);
        }, error => {
          this.toastr.warning("Something went wrong downloading the preview image :(");
        })
      })
    });
  }


  addComment() {
    this.comment.userName = this.authService.getCurrentUser().username;
    console.log(this.comment);
    this.caffService.addComment(this.comment, this.caff.id).subscribe((comment: Comment) => {
      this.caff.comments.push(comment);
    });
    this.comment.comment = "";
  }

  downloadCaff() {
    this.caffService.downloadCaff(this.caff.id).subscribe(data => {
        saveAs(data, "download.caff");
      }, error => {
        this.toastr.error("Something went wrong downloading the image :(");
      }
    );
  }
}
