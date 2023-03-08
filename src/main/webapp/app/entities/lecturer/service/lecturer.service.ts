import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILecturer, getLecturerIdentifier } from '../lecturer.model';

export type EntityResponseType = HttpResponse<ILecturer>;
export type EntityArrayResponseType = HttpResponse<ILecturer[]>;

@Injectable({ providedIn: 'root' })
export class LecturerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lecturers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lecturer: ILecturer): Observable<EntityResponseType> {
    return this.http.post<ILecturer>(this.resourceUrl, lecturer, { observe: 'response' });
  }

  update(lecturer: ILecturer): Observable<EntityResponseType> {
    return this.http.put<ILecturer>(`${this.resourceUrl}/${getLecturerIdentifier(lecturer) as number}`, lecturer, { observe: 'response' });
  }

  partialUpdate(lecturer: ILecturer): Observable<EntityResponseType> {
    return this.http.patch<ILecturer>(`${this.resourceUrl}/${getLecturerIdentifier(lecturer) as number}`, lecturer, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILecturer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILecturer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLecturerToCollectionIfMissing(lecturerCollection: ILecturer[], ...lecturersToCheck: (ILecturer | null | undefined)[]): ILecturer[] {
    const lecturers: ILecturer[] = lecturersToCheck.filter(isPresent);
    if (lecturers.length > 0) {
      const lecturerCollectionIdentifiers = lecturerCollection.map(lecturerItem => getLecturerIdentifier(lecturerItem)!);
      const lecturersToAdd = lecturers.filter(lecturerItem => {
        const lecturerIdentifier = getLecturerIdentifier(lecturerItem);
        if (lecturerIdentifier == null || lecturerCollectionIdentifiers.includes(lecturerIdentifier)) {
          return false;
        }
        lecturerCollectionIdentifiers.push(lecturerIdentifier);
        return true;
      });
      return [...lecturersToAdd, ...lecturerCollection];
    }
    return lecturerCollection;
  }
}
