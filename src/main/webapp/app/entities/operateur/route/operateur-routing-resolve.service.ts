import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOperateur } from '../operateur.model';
import { OperateurService } from '../service/operateur.service';

const operateurResolve = (route: ActivatedRouteSnapshot): Observable<null | IOperateur> => {
  const id = route.params.id;
  if (id) {
    return inject(OperateurService)
      .find(id)
      .pipe(
        mergeMap((operateur: HttpResponse<IOperateur>) => {
          if (operateur.body) {
            return of(operateur.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default operateurResolve;
