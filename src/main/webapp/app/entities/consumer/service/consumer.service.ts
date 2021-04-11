import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsumer, getConsumerIdentifier } from '../consumer.model';

export type EntityResponseType = HttpResponse<IConsumer>;
export type EntityArrayResponseType = HttpResponse<IConsumer[]>;

@Injectable({ providedIn: 'root' })
export class ConsumerService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/consumers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(consumer: IConsumer): Observable<EntityResponseType> {
    return this.http.post<IConsumer>(this.resourceUrl, consumer, { observe: 'response' });
  }

  update(consumer: IConsumer): Observable<EntityResponseType> {
    return this.http.put<IConsumer>(`${this.resourceUrl}/${getConsumerIdentifier(consumer) as number}`, consumer, { observe: 'response' });
  }

  partialUpdate(consumer: IConsumer): Observable<EntityResponseType> {
    return this.http.patch<IConsumer>(`${this.resourceUrl}/${getConsumerIdentifier(consumer) as number}`, consumer, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConsumer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConsumer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConsumerToCollectionIfMissing(consumerCollection: IConsumer[], ...consumersToCheck: (IConsumer | null | undefined)[]): IConsumer[] {
    const consumers: IConsumer[] = consumersToCheck.filter(isPresent);
    if (consumers.length > 0) {
      const consumerCollectionIdentifiers = consumerCollection.map(consumerItem => getConsumerIdentifier(consumerItem)!);
      const consumersToAdd = consumers.filter(consumerItem => {
        const consumerIdentifier = getConsumerIdentifier(consumerItem);
        if (consumerIdentifier == null || consumerCollectionIdentifiers.includes(consumerIdentifier)) {
          return false;
        }
        consumerCollectionIdentifiers.push(consumerIdentifier);
        return true;
      });
      return [...consumersToAdd, ...consumerCollection];
    }
    return consumerCollection;
  }
}
