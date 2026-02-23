import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AlerteLigneArretResolve from './route/alerte-ligne-arret-routing-resolve.service';

const alerteLigneArretRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/alerte-ligne-arret.component').then(m => m.AlerteLigneArretComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/alerte-ligne-arret-detail.component').then(m => m.AlerteLigneArretDetailComponent),
    resolve: {
      alerteLigneArret: AlerteLigneArretResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/alerte-ligne-arret-update.component').then(m => m.AlerteLigneArretUpdateComponent),
    resolve: {
      alerteLigneArret: AlerteLigneArretResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/alerte-ligne-arret-update.component').then(m => m.AlerteLigneArretUpdateComponent),
    resolve: {
      alerteLigneArret: AlerteLigneArretResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default alerteLigneArretRoute;
