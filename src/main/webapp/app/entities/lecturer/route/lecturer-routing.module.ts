import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LecturerComponent } from '../list/lecturer.component';
import { LecturerDetailComponent } from '../detail/lecturer-detail.component';
import { LecturerUpdateComponent } from '../update/lecturer-update.component';
import { LecturerRoutingResolveService } from './lecturer-routing-resolve.service';

const lecturerRoute: Routes = [
  {
    path: '',
    component: LecturerComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LecturerDetailComponent,
    resolve: {
      lecturer: LecturerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LecturerUpdateComponent,
    resolve: {
      lecturer: LecturerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LecturerUpdateComponent,
    resolve: {
      lecturer: LecturerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lecturerRoute)],
  exports: [RouterModule],
})
export class LecturerRoutingModule {}
