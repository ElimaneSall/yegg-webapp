import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import OperateurResolve from './route/operateur-routing-resolve.service';

const operateurRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/operateur.component').then(m => m.OperateurComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/operateur-detail.component').then(m => m.OperateurDetailComponent),
    resolve: {
      operateur: OperateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/operateur-update.component').then(m => m.OperateurUpdateComponent),
    resolve: {
      operateur: OperateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/operateur-update.component').then(m => m.OperateurUpdateComponent),
    resolve: {
      operateur: OperateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default operateurRoute;
