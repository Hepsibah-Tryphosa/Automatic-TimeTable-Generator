import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICollegeInfo, getCollegeInfoIdentifier } from '../college-info.model';

export type EntityResponseType = HttpResponse<ICollegeInfo>;
export type EntityArrayResponseType = HttpResponse<ICollegeInfo[]>;

@Injectable({ providedIn: 'root' })
export class CollegeInfoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/college-infos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(collegeInfo: ICollegeInfo): Observable<EntityResponseType> {
    return this.http.post<ICollegeInfo>(this.resourceUrl, collegeInfo, { observe: 'response' });
  }

  update(collegeInfo: ICollegeInfo): Observable<EntityResponseType> {
    return this.http.put<ICollegeInfo>(`${this.resourceUrl}/${getCollegeInfoIdentifier(collegeInfo) as number}`, collegeInfo, {
      observe: 'response',
    });
  }

  partialUpdate(collegeInfo: ICollegeInfo): Observable<EntityResponseType> {
    return this.http.patch<ICollegeInfo>(`${this.resourceUrl}/${getCollegeInfoIdentifier(collegeInfo) as number}`, collegeInfo, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICollegeInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICollegeInfo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCollegeInfoToCollectionIfMissing(
    collegeInfoCollection: ICollegeInfo[],
    ...collegeInfosToCheck: (ICollegeInfo | null | undefined)[]
  ): ICollegeInfo[] {
    const collegeInfos: ICollegeInfo[] = collegeInfosToCheck.filter(isPresent);
    if (collegeInfos.length > 0) {
      const collegeInfoCollectionIdentifiers = collegeInfoCollection.map(collegeInfoItem => getCollegeInfoIdentifier(collegeInfoItem)!);
      const collegeInfosToAdd = collegeInfos.filter(collegeInfoItem => {
        const collegeInfoIdentifier = getCollegeInfoIdentifier(collegeInfoItem);
        if (collegeInfoIdentifier == null || collegeInfoCollectionIdentifiers.includes(collegeInfoIdentifier)) {
          return false;
        }
        collegeInfoCollectionIdentifiers.push(collegeInfoIdentifier);
        return true;
      });
      return [...collegeInfosToAdd, ...collegeInfoCollection];
    }
    return collegeInfoCollection;
  }
}
