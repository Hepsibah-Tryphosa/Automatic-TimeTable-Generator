import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GenerateTimeTableComponent } from '../list/generate-time-table.component';
import { GenerateTimeTableDetailComponent } from '../detail/generate-time-table-detail.component';
import { GenerateTimeTableUpdateComponent } from '../update/generate-time-table-update.component';
import { GenerateTimeTableRoutingResolveService } from './generate-time-table-routing-resolve.service';

const generateTimeTableRoute: Routes = [
  {
    path: '',
    component: GenerateTimeTableComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GenerateTimeTableDetailComponent,
    resolve: {
      generateTimeTable: GenerateTimeTableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GenerateTimeTableUpdateComponent,
    resolve: {
      generateTimeTable: GenerateTimeTableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GenerateTimeTableUpdateComponent,
    resolve: {
      generateTimeTable: GenerateTimeTableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(generateTimeTableRoute)],
  exports: [RouterModule],
})
export class GenerateTimeTableRoutingModule {}
