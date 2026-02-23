import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlerteApproche } from '../alerte-approche.model';
import { AlerteApprocheService } from '../service/alerte-approche.service';

const alerteApprocheResolve = (route: ActivatedRouteSnapshot): Observable<null | IAlerteApproche> => {
  const id = route.params.id;
  if (id) {
    return inject(AlerteApprocheService)
      .find(id)
      .pipe(
        mergeMap((alerteApproche: HttpResponse<IAlerteApproche>) => {
          if (alerteApproche.body) {
            return of(alerteApproche.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default alerteApprocheResolve;
