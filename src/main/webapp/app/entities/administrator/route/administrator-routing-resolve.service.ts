import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdministrator, Administrator } from '../administrator.model';
import { AdministratorService } from '../service/administrator.service';

@Injectable({ providedIn: 'root' })
export class AdministratorRoutingResolveService implements Resolve<IAdministrator> {
  constructor(protected service: AdministratorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdministrator> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((administrator: HttpResponse<Administrator>) => {
          if (administrator.body) {
            return of(administrator.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Administrator());
  }
}
