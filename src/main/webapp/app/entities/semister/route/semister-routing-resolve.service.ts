import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISemister, Semister } from '../semister.model';
import { SemisterService } from '../service/semister.service';

@Injectable({ providedIn: 'root' })
export class SemisterRoutingResolveService implements Resolve<ISemister> {
  constructor(protected service: SemisterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISemister> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((semister: HttpResponse<Semister>) => {
          if (semister.body) {
            return of(semister.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Semister());
  }
}
