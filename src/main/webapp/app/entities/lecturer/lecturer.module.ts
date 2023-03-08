import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LecturerComponent } from './list/lecturer.component';
import { LecturerDetailComponent } from './detail/lecturer-detail.component';
import { LecturerUpdateComponent } from './update/lecturer-update.component';
import { LecturerDeleteDialogComponent } from './delete/lecturer-delete-dialog.component';
import { LecturerRoutingModule } from './route/lecturer-routing.module';

@NgModule({
  imports: [SharedModule, LecturerRoutingModule],
  declarations: [LecturerComponent, LecturerDetailComponent, LecturerUpdateComponent, LecturerDeleteDialogComponent],
  entryComponents: [LecturerDeleteDialogComponent],
})
export class LecturerModule {}
