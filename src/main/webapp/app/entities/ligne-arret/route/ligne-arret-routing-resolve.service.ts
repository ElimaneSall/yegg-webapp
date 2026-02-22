import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILigneArret } from '../ligne-arret.model';
import { LigneArretService } from '../service/ligne-arret.service';

const ligneArretResolve = (route: ActivatedRouteSnapshot): Observable<null | ILigneArret> => {
  const id = route.params.id;
  if (id) {
    return inject(LigneArretService)
      .find(id)
      .pipe(
        mergeMap((ligneArret: HttpResponse<ILigneArret>) => {
          if (ligneArret.body) {
            return of(ligneArret.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ligneArretResolve;
