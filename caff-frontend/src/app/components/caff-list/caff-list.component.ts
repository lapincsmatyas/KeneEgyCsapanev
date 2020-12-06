import {Component, OnInit} from '@angular/core';
import {CaffService} from "../../services/caff-service.service";
import {Caff} from "../../models/caff";
import {catchError, map} from "rxjs/operators";
import {HttpErrorResponse, HttpEventType} from "@angular/common/http";
import {of} from "rxjs";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-caff-list',
  templateUrl: './caff-list.component.html',
  styleUrls: ['./caff-list.component.css']
})
export class CaffListComponent implements OnInit {
  public caffList: Caff[];
  private fileToUpload: File = null;
  public uploading = false;

  constructor(private caffService: CaffService, private sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    this.caffService.getAllCaffs().subscribe((result) => {
      this.caffList = result;
      this.caffList.forEach((caff: Caff) => {
        this.caffService.getCaffPreview(caff.id).subscribe((preview) => {
          let objectURL = URL.createObjectURL(preview);
          caff.preview = this.sanitizer.bypassSecurityTrustUrl(objectURL);
        })
      })
    })
  }

  uploadFile() {
    if(!this.fileToUpload)
      return;

    this.uploading = true;
    this.caffService.uploadCaff(this.fileToUpload).subscribe( (caff: Caff) => {
      this.caffList.push(caff);
      this.uploading = false;
      this.caffService.getCaffPreview(caff.id).subscribe((preview) => {
        let objectURL = URL.createObjectURL(preview);
        caff.preview = this.sanitizer.bypassSecurityTrustUrl(objectURL);
      }, error => {
        alert("Something went wrong downloading the preview image :(");
        this.uploading = false;
      })
      }, error => {
        alert("Something went wrong during the upload :( ");
      this.uploading = false;
      }
    )
  }

  handleFileInput(files: FileList) {
    this.fileToUpload = files.item(0);
  }
}
