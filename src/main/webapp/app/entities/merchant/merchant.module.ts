import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MerchantComponent } from './list/merchant.component';
import { MerchantDetailComponent } from './detail/merchant-detail.component';
import { MerchantUpdateComponent } from './update/merchant-update.component';
import { MerchantDeleteDialogComponent } from './delete/merchant-delete-dialog.component';
import { MerchantRoutingModule } from './route/merchant-routing.module';

@NgModule({
  imports: [SharedModule, MerchantRoutingModule],
  declarations: [MerchantComponent, MerchantDetailComponent, MerchantUpdateComponent, MerchantDeleteDialogComponent],
  entryComponents: [MerchantDeleteDialogComponent],
})
export class MerchantModule {}
