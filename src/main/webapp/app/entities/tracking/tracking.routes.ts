import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TrackingResolve from './route/tracking-routing-resolve.service';

const trackingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tracking.component').then(m => m.TrackingComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tracking-detail.component').then(m => m.TrackingDetailComponent),
    resolve: {
      tracking: TrackingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tracking-update.component').then(m => m.TrackingUpdateComponent),
    resolve: {
      tracking: TrackingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tracking-update.component').then(m => m.TrackingUpdateComponent),
    resolve: {
      tracking: TrackingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trackingRoute;
