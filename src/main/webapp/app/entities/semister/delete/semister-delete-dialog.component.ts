import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISemister } from '../semister.model';
import { SemisterService } from '../service/semister.service';

@Component({
  templateUrl: './semister-delete-dialog.component.html',
})
export class SemisterDeleteDialogComponent {
  semister?: ISemister;

  constructor(protected semisterService: SemisterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.semisterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
