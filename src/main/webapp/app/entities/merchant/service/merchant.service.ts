import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMerchant, getMerchantIdentifier } from '../merchant.model';

export type EntityResponseType = HttpResponse<IMerchant>;
export type EntityArrayResponseType = HttpResponse<IMerchant[]>;

@Injectable({ providedIn: 'root' })
export class MerchantService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/merchants');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(merchant: IMerchant): Observable<EntityResponseType> {
    return this.http.post<IMerchant>(this.resourceUrl, merchant, { observe: 'response' });
  }

  update(merchant: IMerchant): Observable<EntityResponseType> {
    return this.http.put<IMerchant>(`${this.resourceUrl}/${getMerchantIdentifier(merchant) as number}`, merchant, { observe: 'response' });
  }

  partialUpdate(merchant: IMerchant): Observable<EntityResponseType> {
    return this.http.patch<IMerchant>(`${this.resourceUrl}/${getMerchantIdentifier(merchant) as number}`, merchant, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMerchant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMerchant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMerchantToCollectionIfMissing(merchantCollection: IMerchant[], ...merchantsToCheck: (IMerchant | null | undefined)[]): IMerchant[] {
    const merchants: IMerchant[] = merchantsToCheck.filter(isPresent);
    if (merchants.length > 0) {
      const merchantCollectionIdentifiers = merchantCollection.map(merchantItem => getMerchantIdentifier(merchantItem)!);
      const merchantsToAdd = merchants.filter(merchantItem => {
        const merchantIdentifier = getMerchantIdentifier(merchantItem);
        if (merchantIdentifier == null || merchantCollectionIdentifiers.includes(merchantIdentifier)) {
          return false;
        }
        merchantCollectionIdentifiers.push(merchantIdentifier);
        return true;
      });
      return [...merchantsToAdd, ...merchantCollection];
    }
    return merchantCollection;
  }
}
