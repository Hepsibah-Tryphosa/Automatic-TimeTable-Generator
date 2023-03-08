import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGenerateTimeTable } from '../generate-time-table.model';

@Component({
  selector: 'jhi-generate-time-table-detail',
  templateUrl: './generate-time-table-detail.component.html',
})
export class GenerateTimeTableDetailComponent implements OnInit {
  generateTimeTable: IGenerateTimeTable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ generateTimeTable }) => {
      this.generateTimeTable = generateTimeTable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
