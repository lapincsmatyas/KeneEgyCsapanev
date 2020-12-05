import {Component, OnInit} from '@angular/core';
import {CaffService} from "../../services/caff-service.service";
import {Caff} from "../../models/caff";
import {catchError, map} from "rxjs/operators";
import {HttpErrorResponse, HttpEventType} from "@angular/common/http";
import {of} from "rxjs";

@Component({
  selector: 'app-caff-list',
  templateUrl: './caff-list.component.html',
  styleUrls: ['./caff-list.component.css']
})
export class CaffListComponent implements OnInit {
  public caffList: Caff[];
  private fileToUpload: File = null;

  constructor(private caffService: CaffService) {
  }

  ngOnInit(): void {
    this.caffService.getAllCaffs().subscribe((result) => {
      this.caffList = result;
    })
  }

  uploadFile() {
    if(!this.fileToUpload)
      return;

    this.caffService.uploadCaff(this.fileToUpload).subscribe( (caff: Caff) => {
      this.caffList.push(caff);
      }, error => {
        console.log(error);
      }
    )
  }

  handleFileInput(files: FileList) {
    this.fileToUpload = files.item(0);
  }
}
