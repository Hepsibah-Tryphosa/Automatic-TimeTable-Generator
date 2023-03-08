import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILecturer } from '../lecturer.model';
import { LecturerService } from '../service/lecturer.service';
import { LecturerDeleteDialogComponent } from '../delete/lecturer-delete-dialog.component';

@Component({
  selector: 'jhi-lecturer',
  templateUrl: './lecturer.component.html',
})
export class LecturerComponent implements OnInit {
  lecturers?: ILecturer[];
  isLoading = false;

  constructor(protected lecturerService: LecturerService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lecturerService.query().subscribe({
      next: (res: HttpResponse<ILecturer[]>) => {
        this.isLoading = false;
        this.lecturers = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ILecturer): number {
    return item.id!;
  }

  delete(lecturer: ILecturer): void {
    const modalRef = this.modalService.open(LecturerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lecturer = lecturer;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
