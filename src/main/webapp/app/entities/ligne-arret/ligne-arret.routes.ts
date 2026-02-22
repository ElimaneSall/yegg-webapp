import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LigneArretResolve from './route/ligne-arret-routing-resolve.service';

const ligneArretRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ligne-arret.component').then(m => m.LigneArretComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ligne-arret-detail.component').then(m => m.LigneArretDetailComponent),
    resolve: {
      ligneArret: LigneArretResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ligne-arret-update.component').then(m => m.LigneArretUpdateComponent),
    resolve: {
      ligneArret: LigneArretResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ligne-arret-update.component').then(m => m.LigneArretUpdateComponent),
    resolve: {
      ligneArret: LigneArretResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ligneArretRoute;
