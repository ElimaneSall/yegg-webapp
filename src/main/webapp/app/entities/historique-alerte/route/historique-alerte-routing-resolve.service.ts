import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHistoriqueAlerte } from '../historique-alerte.model';
import { HistoriqueAlerteService } from '../service/historique-alerte.service';

const historiqueAlerteResolve = (route: ActivatedRouteSnapshot): Observable<null | IHistoriqueAlerte> => {
  const id = route.params.id;
  if (id) {
    return inject(HistoriqueAlerteService)
      .find(id)
      .pipe(
        mergeMap((historiqueAlerte: HttpResponse<IHistoriqueAlerte>) => {
          if (historiqueAlerte.body) {
            return of(historiqueAlerte.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default historiqueAlerteResolve;
