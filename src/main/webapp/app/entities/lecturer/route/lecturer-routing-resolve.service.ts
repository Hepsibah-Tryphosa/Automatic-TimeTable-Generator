import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILecturer, Lecturer } from '../lecturer.model';
import { LecturerService } from '../service/lecturer.service';

@Injectable({ providedIn: 'root' })
export class LecturerRoutingResolveService implements Resolve<ILecturer> {
  constructor(protected service: LecturerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILecturer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lecturer: HttpResponse<Lecturer>) => {
          if (lecturer.body) {
            return of(lecturer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Lecturer());
  }
}
