import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BusResolve from './route/bus-routing-resolve.service';

const busRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bus.component').then(m => m.BusComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bus-detail.component').then(m => m.BusDetailComponent),
    resolve: {
      bus: BusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bus-update.component').then(m => m.BusUpdateComponent),
    resolve: {
      bus: BusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bus-update.component').then(m => m.BusUpdateComponent),
    resolve: {
      bus: BusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default busRoute;
