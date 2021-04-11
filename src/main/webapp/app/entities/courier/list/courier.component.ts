import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourier } from '../courier.model';
import { CourierService } from '../service/courier.service';
import { CourierDeleteDialogComponent } from '../delete/courier-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-courier',
  templateUrl: './courier.component.html',
})
export class CourierComponent implements OnInit {
  couriers?: ICourier[];
  isLoading = false;

  constructor(protected courierService: CourierService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.courierService.query().subscribe(
      (res: HttpResponse<ICourier[]>) => {
        this.isLoading = false;
        this.couriers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICourier): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(courier: ICourier): void {
    const modalRef = this.modalService.open(CourierDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.courier = courier;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
