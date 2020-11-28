import { Component, OnInit } from '@angular/core';
import {CaffService} from "../../services/caff-service.service";
import {Caff} from "../../models/caff";

@Component({
  selector: 'app-caff-list',
  templateUrl: './caff-list.component.html',
  styleUrls: ['./caff-list.component.css']
})
export class CaffListComponent implements OnInit {
  public caffList: Caff[];

  constructor(private caffService: CaffService) { }

  ngOnInit(): void {
    this.caffService.getAllCaffs().subscribe((result) => {
      this.caffList = result;
    })
  }

}
