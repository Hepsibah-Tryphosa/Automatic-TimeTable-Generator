import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILecturer } from '../lecturer.model';
import { LecturerService } from '../service/lecturer.service';

@Component({
  templateUrl: './lecturer-delete-dialog.component.html',
})
export class LecturerDeleteDialogComponent {
  lecturer?: ILecturer;

  constructor(protected lecturerService: LecturerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lecturerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
