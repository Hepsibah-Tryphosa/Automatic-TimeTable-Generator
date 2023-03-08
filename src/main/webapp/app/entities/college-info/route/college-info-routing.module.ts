import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CollegeInfoComponent } from '../list/college-info.component';
import { CollegeInfoDetailComponent } from '../detail/college-info-detail.component';
import { CollegeInfoUpdateComponent } from '../update/college-info-update.component';
import { CollegeInfoRoutingResolveService } from './college-info-routing-resolve.service';

const collegeInfoRoute: Routes = [
  {
    path: '',
    component: CollegeInfoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CollegeInfoDetailComponent,
    resolve: {
      collegeInfo: CollegeInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CollegeInfoUpdateComponent,
    resolve: {
      collegeInfo: CollegeInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CollegeInfoUpdateComponent,
    resolve: {
      collegeInfo: CollegeInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(collegeInfoRoute)],
  exports: [RouterModule],
})
export class CollegeInfoRoutingModule {}
