import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdministrator } from '../administrator.model';
import { AdministratorService } from '../service/administrator.service';
import { AdministratorDeleteDialogComponent } from '../delete/administrator-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-administrator',
  templateUrl: './administrator.component.html',
})
export class AdministratorComponent implements OnInit {
  administrators?: IAdministrator[];
  isLoading = false;

  constructor(protected administratorService: AdministratorService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.administratorService.query().subscribe(
      (res: HttpResponse<IAdministrator[]>) => {
        this.isLoading = false;
        this.administrators = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAdministrator): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(administrator: IAdministrator): void {
    const modalRef = this.modalService.open(AdministratorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.administrator = administrator;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
