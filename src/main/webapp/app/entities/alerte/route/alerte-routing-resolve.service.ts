import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlerte } from '../alerte.model';
import { AlerteService } from '../service/alerte.service';

const alerteResolve = (route: ActivatedRouteSnapshot): Observable<null | IAlerte> => {
  const id = route.params.id;
  if (id) {
    return inject(AlerteService)
      .find(id)
      .pipe(
        mergeMap((alerte: HttpResponse<IAlerte>) => {
          if (alerte.body) {
            return of(alerte.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default alerteResolve;
