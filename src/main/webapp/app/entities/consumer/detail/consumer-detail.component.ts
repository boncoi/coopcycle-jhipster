import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConsumer } from '../consumer.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-consumer-detail',
  templateUrl: './consumer-detail.component.html',
})
export class ConsumerDetailComponent implements OnInit {
  consumer: IConsumer | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consumer }) => {
      this.consumer = consumer;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
