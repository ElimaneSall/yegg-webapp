import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArret } from '../arret.model';
import { ArretService } from '../service/arret.service';

const arretResolve = (route: ActivatedRouteSnapshot): Observable<null | IArret> => {
  const id = route.params.id;
  if (id) {
    return inject(ArretService)
      .find(id)
      .pipe(
        mergeMap((arret: HttpResponse<IArret>) => {
          if (arret.body) {
            return of(arret.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default arretResolve;
