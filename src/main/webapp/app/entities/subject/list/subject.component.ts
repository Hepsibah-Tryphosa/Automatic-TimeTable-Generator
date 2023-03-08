import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubject } from '../subject.model';
import { SubjectService } from '../service/subject.service';
import { SubjectDeleteDialogComponent } from '../delete/subject-delete-dialog.component';

@Component({
  selector: 'jhi-subject',
  templateUrl: './subject.component.html',
})
export class SubjectComponent implements OnInit {
  subjects?: ISubject[];
  isLoading = false;

  constructor(protected subjectService: SubjectService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.subjectService.query().subscribe({
      next: (res: HttpResponse<ISubject[]>) => {
        this.isLoading = false;
        this.subjects = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISubject): number {
    return item.id!;
  }

  delete(subject: ISubject): void {
    const modalRef = this.modalService.open(SubjectDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.subject = subject;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
