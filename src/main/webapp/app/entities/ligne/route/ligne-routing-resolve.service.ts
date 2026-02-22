import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILigne } from '../ligne.model';
import { LigneService } from '../service/ligne.service';

const ligneResolve = (route: ActivatedRouteSnapshot): Observable<null | ILigne> => {
  const id = route.params.id;
  if (id) {
    return inject(LigneService)
      .find(id)
      .pipe(
        mergeMap((ligne: HttpResponse<ILigne>) => {
          if (ligne.body) {
            return of(ligne.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ligneResolve;
