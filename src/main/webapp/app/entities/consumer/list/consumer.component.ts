import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConsumer } from '../consumer.model';
import { ConsumerService } from '../service/consumer.service';
import { ConsumerDeleteDialogComponent } from '../delete/consumer-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-consumer',
  templateUrl: './consumer.component.html',
})
export class ConsumerComponent implements OnInit {
  consumers?: IConsumer[];
  isLoading = false;

  constructor(protected consumerService: ConsumerService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.consumerService.query().subscribe(
      (res: HttpResponse<IConsumer[]>) => {
        this.isLoading = false;
        this.consumers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IConsumer): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(consumer: IConsumer): void {
    const modalRef = this.modalService.open(ConsumerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.consumer = consumer;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
