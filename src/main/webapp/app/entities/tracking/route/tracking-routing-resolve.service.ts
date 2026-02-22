import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITracking } from '../tracking.model';
import { TrackingService } from '../service/tracking.service';

const trackingResolve = (route: ActivatedRouteSnapshot): Observable<null | ITracking> => {
  const id = route.params.id;
  if (id) {
    return inject(TrackingService)
      .find(id)
      .pipe(
        mergeMap((tracking: HttpResponse<ITracking>) => {
          if (tracking.body) {
            return of(tracking.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default trackingResolve;
