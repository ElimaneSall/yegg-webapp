import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LigneResolve from './route/ligne-routing-resolve.service';

const ligneRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ligne.component').then(m => m.LigneComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ligne-detail.component').then(m => m.LigneDetailComponent),
    resolve: {
      ligne: LigneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ligne-update.component').then(m => m.LigneUpdateComponent),
    resolve: {
      ligne: LigneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ligne-update.component').then(m => m.LigneUpdateComponent),
    resolve: {
      ligne: LigneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ligneRoute;
