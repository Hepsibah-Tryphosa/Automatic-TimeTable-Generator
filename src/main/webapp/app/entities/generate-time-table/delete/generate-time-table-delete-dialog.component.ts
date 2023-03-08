import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGenerateTimeTable } from '../generate-time-table.model';
import { GenerateTimeTableService } from '../service/generate-time-table.service';

@Component({
  templateUrl: './generate-time-table-delete-dialog.component.html',
})
export class GenerateTimeTableDeleteDialogComponent {
  generateTimeTable?: IGenerateTimeTable;

  constructor(protected generateTimeTableService: GenerateTimeTableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.generateTimeTableService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
