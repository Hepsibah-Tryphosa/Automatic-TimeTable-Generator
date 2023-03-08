import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGenerateTimeTable } from '../generate-time-table.model';
import { GenerateTimeTableService } from '../service/generate-time-table.service';
import { GenerateTimeTableDeleteDialogComponent } from '../delete/generate-time-table-delete-dialog.component';

@Component({
  selector: 'jhi-generate-time-table',
  templateUrl: './generate-time-table.component.html',
})
export class GenerateTimeTableComponent implements OnInit {
  generateTimeTables?: IGenerateTimeTable[];
  isLoading = false;

  constructor(protected generateTimeTableService: GenerateTimeTableService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.generateTimeTableService.query().subscribe({
      next: (res: HttpResponse<IGenerateTimeTable[]>) => {
        this.isLoading = false;
        this.generateTimeTables = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IGenerateTimeTable): number {
    return item.id!;
  }

  delete(generateTimeTable: IGenerateTimeTable): void {
    const modalRef = this.modalService.open(GenerateTimeTableDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.generateTimeTable = generateTimeTable;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
