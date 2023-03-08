import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SemisterComponent } from './list/semister.component';
import { SemisterDetailComponent } from './detail/semister-detail.component';
import { SemisterUpdateComponent } from './update/semister-update.component';
import { SemisterDeleteDialogComponent } from './delete/semister-delete-dialog.component';
import { SemisterRoutingModule } from './route/semister-routing.module';

@NgModule({
  imports: [SharedModule, SemisterRoutingModule],
  declarations: [SemisterComponent, SemisterDetailComponent, SemisterUpdateComponent, SemisterDeleteDialogComponent],
  entryComponents: [SemisterDeleteDialogComponent],
})
export class SemisterModule {}
