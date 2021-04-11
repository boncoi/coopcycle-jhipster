import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AdministratorComponent } from './list/administrator.component';
import { AdministratorDetailComponent } from './detail/administrator-detail.component';
import { AdministratorUpdateComponent } from './update/administrator-update.component';
import { AdministratorDeleteDialogComponent } from './delete/administrator-delete-dialog.component';
import { AdministratorRoutingModule } from './route/administrator-routing.module';

@NgModule({
  imports: [SharedModule, AdministratorRoutingModule],
  declarations: [AdministratorComponent, AdministratorDetailComponent, AdministratorUpdateComponent, AdministratorDeleteDialogComponent],
  entryComponents: [AdministratorDeleteDialogComponent],
})
export class AdministratorModule {}
