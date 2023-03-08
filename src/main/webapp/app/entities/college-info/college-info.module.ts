import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CollegeInfoComponent } from './list/college-info.component';
import { CollegeInfoDetailComponent } from './detail/college-info-detail.component';
import { CollegeInfoUpdateComponent } from './update/college-info-update.component';
import { CollegeInfoDeleteDialogComponent } from './delete/college-info-delete-dialog.component';
import { CollegeInfoRoutingModule } from './route/college-info-routing.module';

@NgModule({
  imports: [SharedModule, CollegeInfoRoutingModule],
  declarations: [CollegeInfoComponent, CollegeInfoDetailComponent, CollegeInfoUpdateComponent, CollegeInfoDeleteDialogComponent],
  entryComponents: [CollegeInfoDeleteDialogComponent],
})
export class CollegeInfoModule {}
