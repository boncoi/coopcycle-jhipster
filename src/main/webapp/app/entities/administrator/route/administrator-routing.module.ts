import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AdministratorComponent } from '../list/administrator.component';
import { AdministratorDetailComponent } from '../detail/administrator-detail.component';
import { AdministratorUpdateComponent } from '../update/administrator-update.component';
import { AdministratorRoutingResolveService } from './administrator-routing-resolve.service';

const administratorRoute: Routes = [
  {
    path: '',
    component: AdministratorComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AdministratorDetailComponent,
    resolve: {
      administrator: AdministratorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AdministratorUpdateComponent,
    resolve: {
      administrator: AdministratorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AdministratorUpdateComponent,
    resolve: {
      administrator: AdministratorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(administratorRoute)],
  exports: [RouterModule],
})
export class AdministratorRoutingModule {}
