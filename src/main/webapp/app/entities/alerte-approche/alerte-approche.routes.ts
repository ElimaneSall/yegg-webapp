import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AlerteApprocheResolve from './route/alerte-approche-routing-resolve.service';

const alerteApprocheRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/alerte-approche.component').then(m => m.AlerteApprocheComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/alerte-approche-detail.component').then(m => m.AlerteApprocheDetailComponent),
    resolve: {
      alerteApproche: AlerteApprocheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/alerte-approche-update.component').then(m => m.AlerteApprocheUpdateComponent),
    resolve: {
      alerteApproche: AlerteApprocheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/alerte-approche-update.component').then(m => m.AlerteApprocheUpdateComponent),
    resolve: {
      alerteApproche: AlerteApprocheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default alerteApprocheRoute;
