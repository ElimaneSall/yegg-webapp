import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlerteLigneArret } from '../alerte-ligne-arret.model';
import { AlerteLigneArretService } from '../service/alerte-ligne-arret.service';

const alerteLigneArretResolve = (route: ActivatedRouteSnapshot): Observable<null | IAlerteLigneArret> => {
  const id = route.params.id;
  if (id) {
    return inject(AlerteLigneArretService)
      .find(id)
      .pipe(
        mergeMap((alerteLigneArret: HttpResponse<IAlerteLigneArret>) => {
          if (alerteLigneArret.body) {
            return of(alerteLigneArret.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default alerteLigneArretResolve;
