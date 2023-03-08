import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISemister } from '../semister.model';

@Component({
  selector: 'jhi-semister-detail',
  templateUrl: './semister-detail.component.html',
})
export class SemisterDetailComponent implements OnInit {
  semister: ISemister | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ semister }) => {
      this.semister = semister;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
