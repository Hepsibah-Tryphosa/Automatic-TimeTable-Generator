import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGenerateTimeTable } from '../generate-time-table.model';
import { GenerateTimeTableService } from '../service/generate-time-table.service';
import { GenerateTimeTableDeleteDialogComponent } from '../delete/generate-time-table-delete-dialog.component';
import { IDailyTimeTable } from '../generate-weekly-timetable.model';

@Component({
  selector: 'jhi-generate-time-table',
  templateUrl: './generate-time-table.component.html',
})
export class GenerateTimeTableComponent implements OnInit {
  generateTimeTables?: IDailyTimeTable[];
  headersTimeTable?: string[];
  isLoading = false;

  constructor(protected generateTimeTableService: GenerateTimeTableService, protected modalService: NgbModal) {}

  loadAll(courseName: string, semName: string, noOfClassesPerDay: string): void {
    this.isLoading = true;

    this.generateTimeTableService.generateTimetable(courseName, semName, noOfClassesPerDay).subscribe({
      next: (res: HttpResponse<IDailyTimeTable[]>) => {
        this.isLoading = false;
        this.generateTimeTables = res.body ?? [];
        const cpd = this.generateTimeTables[0].noOfClassesPerDay ?? 0;
        this.headersTimeTable = new Array(cpd);
        for (let i = 0; i < cpd; i++) {
          this.headersTimeTable[i] = `CLASS-${i + 1}`;
        }
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll('ECE', '1-1', '6');
  }

  trackId(_index: number, item: IGenerateTimeTable): number {
    return item.id!;
  }

  delete(generateTimeTable: IGenerateTimeTable): void {
    const modalRef = this.modalService.open(GenerateTimeTableDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.generateTimeTable = generateTimeTable;
    // unsubscribe not needed because closed completes on modal close
  }
}
