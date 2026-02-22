import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ArretResolve from './route/arret-routing-resolve.service';

const arretRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/arret.component').then(m => m.ArretComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/arret-detail.component').then(m => m.ArretDetailComponent),
    resolve: {
      arret: ArretResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/arret-update.component').then(m => m.ArretUpdateComponent),
    resolve: {
      arret: ArretResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/arret-update.component').then(m => m.ArretUpdateComponent),
    resolve: {
      arret: ArretResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default arretRoute;
