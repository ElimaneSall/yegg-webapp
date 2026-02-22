import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FavoriResolve from './route/favori-routing-resolve.service';

const favoriRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/favori.component').then(m => m.FavoriComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/favori-detail.component').then(m => m.FavoriDetailComponent),
    resolve: {
      favori: FavoriResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/favori-update.component').then(m => m.FavoriUpdateComponent),
    resolve: {
      favori: FavoriResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/favori-update.component').then(m => m.FavoriUpdateComponent),
    resolve: {
      favori: FavoriResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default favoriRoute;
