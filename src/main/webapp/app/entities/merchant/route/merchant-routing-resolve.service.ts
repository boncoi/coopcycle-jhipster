import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMerchant, Merchant } from '../merchant.model';
import { MerchantService } from '../service/merchant.service';

@Injectable({ providedIn: 'root' })
export class MerchantRoutingResolveService implements Resolve<IMerchant> {
  constructor(protected service: MerchantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMerchant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((merchant: HttpResponse<Merchant>) => {
          if (merchant.body) {
            return of(merchant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Merchant());
  }
}
