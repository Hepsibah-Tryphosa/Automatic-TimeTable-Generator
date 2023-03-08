import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGenerateTimeTable, GenerateTimeTable } from '../generate-time-table.model';
import { GenerateTimeTableService } from '../service/generate-time-table.service';

@Injectable({ providedIn: 'root' })
export class GenerateTimeTableRoutingResolveService implements Resolve<IGenerateTimeTable> {
  constructor(protected service: GenerateTimeTableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGenerateTimeTable> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((generateTimeTable: HttpResponse<GenerateTimeTable>) => {
          if (generateTimeTable.body) {
            return of(generateTimeTable.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GenerateTimeTable());
  }
}
