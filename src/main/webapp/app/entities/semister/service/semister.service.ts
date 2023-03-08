import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISemister, getSemisterIdentifier } from '../semister.model';

export type EntityResponseType = HttpResponse<ISemister>;
export type EntityArrayResponseType = HttpResponse<ISemister[]>;

@Injectable({ providedIn: 'root' })
export class SemisterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/semisters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(semister: ISemister): Observable<EntityResponseType> {
    return this.http.post<ISemister>(this.resourceUrl, semister, { observe: 'response' });
  }

  update(semister: ISemister): Observable<EntityResponseType> {
    return this.http.put<ISemister>(`${this.resourceUrl}/${getSemisterIdentifier(semister) as number}`, semister, { observe: 'response' });
  }

  partialUpdate(semister: ISemister): Observable<EntityResponseType> {
    return this.http.patch<ISemister>(`${this.resourceUrl}/${getSemisterIdentifier(semister) as number}`, semister, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISemister>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISemister[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSemisterToCollectionIfMissing(semisterCollection: ISemister[], ...semistersToCheck: (ISemister | null | undefined)[]): ISemister[] {
    const semisters: ISemister[] = semistersToCheck.filter(isPresent);
    if (semisters.length > 0) {
      const semisterCollectionIdentifiers = semisterCollection.map(semisterItem => getSemisterIdentifier(semisterItem)!);
      const semistersToAdd = semisters.filter(semisterItem => {
        const semisterIdentifier = getSemisterIdentifier(semisterItem);
        if (semisterIdentifier == null || semisterCollectionIdentifiers.includes(semisterIdentifier)) {
          return false;
        }
        semisterCollectionIdentifiers.push(semisterIdentifier);
        return true;
      });
      return [...semistersToAdd, ...semisterCollection];
    }
    return semisterCollection;
  }
}
