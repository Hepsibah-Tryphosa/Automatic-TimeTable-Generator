import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICollegeInfo } from '../college-info.model';

@Component({
  selector: 'jhi-college-info-detail',
  templateUrl: './college-info-detail.component.html',
})
export class CollegeInfoDetailComponent implements OnInit {
  collegeInfo: ICollegeInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ collegeInfo }) => {
      this.collegeInfo = collegeInfo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
