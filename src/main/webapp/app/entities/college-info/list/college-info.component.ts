import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICollegeInfo } from '../college-info.model';
import { CollegeInfoService } from '../service/college-info.service';
import { CollegeInfoDeleteDialogComponent } from '../delete/college-info-delete-dialog.component';

@Component({
  selector: 'jhi-college-info',
  templateUrl: './college-info.component.html',
})
export class CollegeInfoComponent implements OnInit {
  collegeInfos?: ICollegeInfo[];
  isLoading = false;

  constructor(protected collegeInfoService: CollegeInfoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.collegeInfoService.query().subscribe({
      next: (res: HttpResponse<ICollegeInfo[]>) => {
        this.isLoading = false;
        this.collegeInfos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICollegeInfo): number {
    return item.id!;
  }

  delete(collegeInfo: ICollegeInfo): void {
    const modalRef = this.modalService.open(CollegeInfoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.collegeInfo = collegeInfo;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
