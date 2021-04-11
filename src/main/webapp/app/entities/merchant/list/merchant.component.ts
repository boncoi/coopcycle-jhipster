import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMerchant } from '../merchant.model';
import { MerchantService } from '../service/merchant.service';
import { MerchantDeleteDialogComponent } from '../delete/merchant-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-merchant',
  templateUrl: './merchant.component.html',
})
export class MerchantComponent implements OnInit {
  merchants?: IMerchant[];
  isLoading = false;

  constructor(protected merchantService: MerchantService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.merchantService.query().subscribe(
      (res: HttpResponse<IMerchant[]>) => {
        this.isLoading = false;
        this.merchants = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMerchant): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(merchant: IMerchant): void {
    const modalRef = this.modalService.open(MerchantDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.merchant = merchant;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
