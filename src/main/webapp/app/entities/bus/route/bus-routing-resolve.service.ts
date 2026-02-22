import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBus } from '../bus.model';
import { BusService } from '../service/bus.service';

const busResolve = (route: ActivatedRouteSnapshot): Observable<null | IBus> => {
  const id = route.params.id;
  if (id) {
    return inject(BusService)
      .find(id)
      .pipe(
        mergeMap((bus: HttpResponse<IBus>) => {
          if (bus.body) {
            return of(bus.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default busResolve;
