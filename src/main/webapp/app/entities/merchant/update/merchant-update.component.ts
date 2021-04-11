import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMerchant, Merchant } from '../merchant.model';
import { MerchantService } from '../service/merchant.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';
import { CooperativeService } from 'app/entities/cooperative/service/cooperative.service';

@Component({
  selector: 'jhi-merchant-update',
  templateUrl: './merchant-update.component.html',
})
export class MerchantUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  cooperativesSharedCollection: ICooperative[] = [];

  editForm = this.fb.group({
    id: [],
    merchantName: [null, [Validators.required]],
    address: [null, [Validators.required]],
    merchantType: [null, [Validators.required]],
    imageProfil: [],
    imageProfilContentType: [],
    mobilePhone: [null, [Validators.required]],
    user: [],
    cooperative: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected merchantService: MerchantService,
    protected userService: UserService,
    protected cooperativeService: CooperativeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ merchant }) => {
      this.updateForm(merchant);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('coopcycleApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const merchant = this.createFromForm();
    if (merchant.id !== undefined) {
      this.subscribeToSaveResponse(this.merchantService.update(merchant));
    } else {
      this.subscribeToSaveResponse(this.merchantService.create(merchant));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackCooperativeById(index: number, item: ICooperative): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMerchant>>): void {
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

  protected updateForm(merchant: IMerchant): void {
    this.editForm.patchValue({
      id: merchant.id,
      merchantName: merchant.merchantName,
      address: merchant.address,
      merchantType: merchant.merchantType,
      imageProfil: merchant.imageProfil,
      imageProfilContentType: merchant.imageProfilContentType,
      mobilePhone: merchant.mobilePhone,
      user: merchant.user,
      cooperative: merchant.cooperative,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, merchant.user);
    this.cooperativesSharedCollection = this.cooperativeService.addCooperativeToCollectionIfMissing(
      this.cooperativesSharedCollection,
      merchant.cooperative
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.cooperativeService
      .query()
      .pipe(map((res: HttpResponse<ICooperative[]>) => res.body ?? []))
      .pipe(
        map((cooperatives: ICooperative[]) =>
          this.cooperativeService.addCooperativeToCollectionIfMissing(cooperatives, this.editForm.get('cooperative')!.value)
        )
      )
      .subscribe((cooperatives: ICooperative[]) => (this.cooperativesSharedCollection = cooperatives));
  }

  protected createFromForm(): IMerchant {
    return {
      ...new Merchant(),
      id: this.editForm.get(['id'])!.value,
      merchantName: this.editForm.get(['merchantName'])!.value,
      address: this.editForm.get(['address'])!.value,
      merchantType: this.editForm.get(['merchantType'])!.value,
      imageProfilContentType: this.editForm.get(['imageProfilContentType'])!.value,
      imageProfil: this.editForm.get(['imageProfil'])!.value,
      mobilePhone: this.editForm.get(['mobilePhone'])!.value,
      user: this.editForm.get(['user'])!.value,
      cooperative: this.editForm.get(['cooperative'])!.value,
    };
  }
}
