import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFavori } from '../favori.model';
import { FavoriService } from '../service/favori.service';

const favoriResolve = (route: ActivatedRouteSnapshot): Observable<null | IFavori> => {
  const id = route.params.id;
  if (id) {
    return inject(FavoriService)
      .find(id)
      .pipe(
        mergeMap((favori: HttpResponse<IFavori>) => {
          if (favori.body) {
            return of(favori.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default favoriResolve;
