import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGenerateTimeTable, getGenerateTimeTableIdentifier } from '../generate-time-table.model';

export type EntityResponseType = HttpResponse<IGenerateTimeTable>;
export type EntityArrayResponseType = HttpResponse<IGenerateTimeTable[]>;

@Injectable({ providedIn: 'root' })
export class GenerateTimeTableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/generate-time-tables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(generateTimeTable: IGenerateTimeTable): Observable<EntityResponseType> {
    return this.http.post<IGenerateTimeTable>(this.resourceUrl, generateTimeTable, { observe: 'response' });
  }

  update(generateTimeTable: IGenerateTimeTable): Observable<EntityResponseType> {
    return this.http.put<IGenerateTimeTable>(
      `${this.resourceUrl}/${getGenerateTimeTableIdentifier(generateTimeTable) as number}`,
      generateTimeTable,
      { observe: 'response' }
    );
  }

  partialUpdate(generateTimeTable: IGenerateTimeTable): Observable<EntityResponseType> {
    return this.http.patch<IGenerateTimeTable>(
      `${this.resourceUrl}/${getGenerateTimeTableIdentifier(generateTimeTable) as number}`,
      generateTimeTable,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGenerateTimeTable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGenerateTimeTable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGenerateTimeTableToCollectionIfMissing(
    generateTimeTableCollection: IGenerateTimeTable[],
    ...generateTimeTablesToCheck: (IGenerateTimeTable | null | undefined)[]
  ): IGenerateTimeTable[] {
    const generateTimeTables: IGenerateTimeTable[] = generateTimeTablesToCheck.filter(isPresent);
    if (generateTimeTables.length > 0) {
      const generateTimeTableCollectionIdentifiers = generateTimeTableCollection.map(
        generateTimeTableItem => getGenerateTimeTableIdentifier(generateTimeTableItem)!
      );
      const generateTimeTablesToAdd = generateTimeTables.filter(generateTimeTableItem => {
        const generateTimeTableIdentifier = getGenerateTimeTableIdentifier(generateTimeTableItem);
        if (generateTimeTableIdentifier == null || generateTimeTableCollectionIdentifiers.includes(generateTimeTableIdentifier)) {
          return false;
        }
        generateTimeTableCollectionIdentifiers.push(generateTimeTableIdentifier);
        return true;
      });
      return [...generateTimeTablesToAdd, ...generateTimeTableCollection];
    }
    return generateTimeTableCollection;
  }
}
