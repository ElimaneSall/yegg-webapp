import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AlerteResolve from './route/alerte-routing-resolve.service';

const alerteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/alerte.component').then(m => m.AlerteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/alerte-detail.component').then(m => m.AlerteDetailComponent),
    resolve: {
      alerte: AlerteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/alerte-update.component').then(m => m.AlerteUpdateComponent),
    resolve: {
      alerte: AlerteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/alerte-update.component').then(m => m.AlerteUpdateComponent),
    resolve: {
      alerte: AlerteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default alerteRoute;
