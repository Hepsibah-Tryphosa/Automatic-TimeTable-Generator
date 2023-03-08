import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GenerateTimeTableComponent } from './list/generate-time-table.component';
import { GenerateTimeTableDetailComponent } from './detail/generate-time-table-detail.component';
import { GenerateTimeTableUpdateComponent } from './update/generate-time-table-update.component';
import { GenerateTimeTableDeleteDialogComponent } from './delete/generate-time-table-delete-dialog.component';
import { GenerateTimeTableRoutingModule } from './route/generate-time-table-routing.module';

@NgModule({
  imports: [SharedModule, GenerateTimeTableRoutingModule],
  declarations: [
    GenerateTimeTableComponent,
    GenerateTimeTableDetailComponent,
    GenerateTimeTableUpdateComponent,
    GenerateTimeTableDeleteDialogComponent,
  ],
  entryComponents: [GenerateTimeTableDeleteDialogComponent],
})
export class GenerateTimeTableModule {}
