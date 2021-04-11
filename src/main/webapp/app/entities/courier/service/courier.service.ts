import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourier, getCourierIdentifier } from '../courier.model';

export type EntityResponseType = HttpResponse<ICourier>;
export type EntityArrayResponseType = HttpResponse<ICourier[]>;

@Injectable({ providedIn: 'root' })
export class CourierService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/couriers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(courier: ICourier): Observable<EntityResponseType> {
    return this.http.post<ICourier>(this.resourceUrl, courier, { observe: 'response' });
  }

  update(courier: ICourier): Observable<EntityResponseType> {
    return this.http.put<ICourier>(`${this.resourceUrl}/${getCourierIdentifier(courier) as number}`, courier, { observe: 'response' });
  }

  partialUpdate(courier: ICourier): Observable<EntityResponseType> {
    return this.http.patch<ICourier>(`${this.resourceUrl}/${getCourierIdentifier(courier) as number}`, courier, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCourierToCollectionIfMissing(courierCollection: ICourier[], ...couriersToCheck: (ICourier | null | undefined)[]): ICourier[] {
    const couriers: ICourier[] = couriersToCheck.filter(isPresent);
    if (couriers.length > 0) {
      const courierCollectionIdentifiers = courierCollection.map(courierItem => getCourierIdentifier(courierItem)!);
      const couriersToAdd = couriers.filter(courierItem => {
        const courierIdentifier = getCourierIdentifier(courierItem);
        if (courierIdentifier == null || courierCollectionIdentifiers.includes(courierIdentifier)) {
          return false;
        }
        courierCollectionIdentifiers.push(courierIdentifier);
        return true;
      });
      return [...couriersToAdd, ...courierCollection];
    }
    return courierCollection;
  }
}
