import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILecturer } from '../lecturer.model';

@Component({
  selector: 'jhi-lecturer-detail',
  templateUrl: './lecturer-detail.component.html',
})
export class LecturerDetailComponent implements OnInit {
  lecturer: ILecturer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lecturer }) => {
      this.lecturer = lecturer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
