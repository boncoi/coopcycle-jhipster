import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdministrator } from '../administrator.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-administrator-detail',
  templateUrl: './administrator-detail.component.html',
})
export class AdministratorDetailComponent implements OnInit {
  administrator: IAdministrator | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ administrator }) => {
      this.administrator = administrator;
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
