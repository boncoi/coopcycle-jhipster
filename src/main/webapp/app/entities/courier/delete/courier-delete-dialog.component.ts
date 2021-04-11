import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourier } from '../courier.model';
import { CourierService } from '../service/courier.service';

@Component({
  templateUrl: './courier-delete-dialog.component.html',
})
export class CourierDeleteDialogComponent {
  courier?: ICourier;

  constructor(protected courierService: CourierService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courierService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
