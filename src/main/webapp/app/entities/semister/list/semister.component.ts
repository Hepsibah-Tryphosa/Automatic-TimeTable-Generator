import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISemister } from '../semister.model';
import { SemisterService } from '../service/semister.service';
import { SemisterDeleteDialogComponent } from '../delete/semister-delete-dialog.component';

@Component({
  selector: 'jhi-semister',
  templateUrl: './semister.component.html',
})
export class SemisterComponent implements OnInit {
  semisters?: ISemister[];
  isLoading = false;

  constructor(protected semisterService: SemisterService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.semisterService.query().subscribe({
      next: (res: HttpResponse<ISemister[]>) => {
        this.isLoading = false;
        this.semisters = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISemister): number {
    return item.id!;
  }

  delete(semister: ISemister): void {
    const modalRef = this.modalService.open(SemisterDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.semister = semister;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
