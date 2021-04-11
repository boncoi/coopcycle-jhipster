import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBill, Bill } from '../bill.model';
import { BillService } from '../service/bill.service';
import { IBasket } from 'app/entities/basket/basket.model';
import { BasketService } from 'app/entities/basket/service/basket.service';

@Component({
  selector: 'jhi-bill-update',
  templateUrl: './bill-update.component.html',
})
export class BillUpdateComponent implements OnInit {
  isSaving = false;

  idBasketsCollection: IBasket[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    payment: [null, [Validators.required]],
    status: [null, [Validators.required]],
    totalPrice: [null, [Validators.required, Validators.min(0)]],
    idBasket: [],
  });

  constructor(
    protected billService: BillService,
    protected basketService: BasketService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bill }) => {
      if (bill.id === undefined) {
        const today = dayjs().startOf('day');
        bill.date = today;
      }

      this.updateForm(bill);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bill = this.createFromForm();
    if (bill.id !== undefined) {
      this.subscribeToSaveResponse(this.billService.update(bill));
    } else {
      this.subscribeToSaveResponse(this.billService.create(bill));
    }
  }

  trackBasketById(index: number, item: IBasket): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBill>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(bill: IBill): void {
    this.editForm.patchValue({
      id: bill.id,
      date: bill.date ? bill.date.format(DATE_TIME_FORMAT) : null,
      payment: bill.payment,
      status: bill.status,
      totalPrice: bill.totalPrice,
      idBasket: bill.idBasket,
    });

    this.idBasketsCollection = this.basketService.addBasketToCollectionIfMissing(this.idBasketsCollection, bill.idBasket);
  }

  protected loadRelationshipsOptions(): void {
    this.basketService
      .query({ filter: 'bill-is-null' })
      .pipe(map((res: HttpResponse<IBasket[]>) => res.body ?? []))
      .pipe(map((baskets: IBasket[]) => this.basketService.addBasketToCollectionIfMissing(baskets, this.editForm.get('idBasket')!.value)))
      .subscribe((baskets: IBasket[]) => (this.idBasketsCollection = baskets));
  }

  protected createFromForm(): IBill {
    return {
      ...new Bill(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      payment: this.editForm.get(['payment'])!.value,
      status: this.editForm.get(['status'])!.value,
      totalPrice: this.editForm.get(['totalPrice'])!.value,
      idBasket: this.editForm.get(['idBasket'])!.value,
    };
  }
}
