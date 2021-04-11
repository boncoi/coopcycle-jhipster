import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdministrator } from '../administrator.model';
import { AdministratorService } from '../service/administrator.service';

@Component({
  templateUrl: './administrator-delete-dialog.component.html',
})
export class AdministratorDeleteDialogComponent {
  administrator?: IAdministrator;

  constructor(protected administratorService: AdministratorService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.administratorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
