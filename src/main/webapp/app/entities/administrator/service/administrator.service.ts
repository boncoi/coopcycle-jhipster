import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdministrator, getAdministratorIdentifier } from '../administrator.model';

export type EntityResponseType = HttpResponse<IAdministrator>;
export type EntityArrayResponseType = HttpResponse<IAdministrator[]>;

@Injectable({ providedIn: 'root' })
export class AdministratorService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/administrators');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(administrator: IAdministrator): Observable<EntityResponseType> {
    return this.http.post<IAdministrator>(this.resourceUrl, administrator, { observe: 'response' });
  }

  update(administrator: IAdministrator): Observable<EntityResponseType> {
    return this.http.put<IAdministrator>(`${this.resourceUrl}/${getAdministratorIdentifier(administrator) as number}`, administrator, {
      observe: 'response',
    });
  }

  partialUpdate(administrator: IAdministrator): Observable<EntityResponseType> {
    return this.http.patch<IAdministrator>(`${this.resourceUrl}/${getAdministratorIdentifier(administrator) as number}`, administrator, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdministrator>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdministrator[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAdministratorToCollectionIfMissing(
    administratorCollection: IAdministrator[],
    ...administratorsToCheck: (IAdministrator | null | undefined)[]
  ): IAdministrator[] {
    const administrators: IAdministrator[] = administratorsToCheck.filter(isPresent);
    if (administrators.length > 0) {
      const administratorCollectionIdentifiers = administratorCollection.map(
        administratorItem => getAdministratorIdentifier(administratorItem)!
      );
      const administratorsToAdd = administrators.filter(administratorItem => {
        const administratorIdentifier = getAdministratorIdentifier(administratorItem);
        if (administratorIdentifier == null || administratorCollectionIdentifiers.includes(administratorIdentifier)) {
          return false;
        }
        administratorCollectionIdentifiers.push(administratorIdentifier);
        return true;
      });
      return [...administratorsToAdd, ...administratorCollection];
    }
    return administratorCollection;
  }
}
