import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MerchantComponent } from '../list/merchant.component';
import { MerchantDetailComponent } from '../detail/merchant-detail.component';
import { MerchantUpdateComponent } from '../update/merchant-update.component';
import { MerchantRoutingResolveService } from './merchant-routing-resolve.service';

const merchantRoute: Routes = [
  {
    path: '',
    component: MerchantComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MerchantDetailComponent,
    resolve: {
      merchant: MerchantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MerchantUpdateComponent,
    resolve: {
      merchant: MerchantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MerchantUpdateComponent,
    resolve: {
      merchant: MerchantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(merchantRoute)],
  exports: [RouterModule],
})
export class MerchantRoutingModule {}
