import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourierComponent } from '../list/courier.component';
import { CourierDetailComponent } from '../detail/courier-detail.component';
import { CourierUpdateComponent } from '../update/courier-update.component';
import { CourierRoutingResolveService } from './courier-routing-resolve.service';

const courierRoute: Routes = [
  {
    path: '',
    component: CourierComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourierDetailComponent,
    resolve: {
      courier: CourierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourierUpdateComponent,
    resolve: {
      courier: CourierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourierUpdateComponent,
    resolve: {
      courier: CourierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(courierRoute)],
  exports: [RouterModule],
})
export class CourierRoutingModule {}
