import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SemisterComponent } from '../list/semister.component';
import { SemisterDetailComponent } from '../detail/semister-detail.component';
import { SemisterUpdateComponent } from '../update/semister-update.component';
import { SemisterRoutingResolveService } from './semister-routing-resolve.service';

const semisterRoute: Routes = [
  {
    path: '',
    component: SemisterComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SemisterDetailComponent,
    resolve: {
      semister: SemisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SemisterUpdateComponent,
    resolve: {
      semister: SemisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SemisterUpdateComponent,
    resolve: {
      semister: SemisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(semisterRoute)],
  exports: [RouterModule],
})
export class SemisterRoutingModule {}
