import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CourierComponent } from './list/courier.component';
import { CourierDetailComponent } from './detail/courier-detail.component';
import { CourierUpdateComponent } from './update/courier-update.component';
import { CourierDeleteDialogComponent } from './delete/courier-delete-dialog.component';
import { CourierRoutingModule } from './route/courier-routing.module';

@NgModule({
  imports: [SharedModule, CourierRoutingModule],
  declarations: [CourierComponent, CourierDetailComponent, CourierUpdateComponent, CourierDeleteDialogComponent],
  entryComponents: [CourierDeleteDialogComponent],
})
export class CourierModule {}
