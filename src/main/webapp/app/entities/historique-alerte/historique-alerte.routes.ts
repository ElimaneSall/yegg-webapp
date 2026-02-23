import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import HistoriqueAlerteResolve from './route/historique-alerte-routing-resolve.service';

const historiqueAlerteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/historique-alerte.component').then(m => m.HistoriqueAlerteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/historique-alerte-detail.component').then(m => m.HistoriqueAlerteDetailComponent),
    resolve: {
      historiqueAlerte: HistoriqueAlerteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/historique-alerte-update.component').then(m => m.HistoriqueAlerteUpdateComponent),
    resolve: {
      historiqueAlerte: HistoriqueAlerteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/historique-alerte-update.component').then(m => m.HistoriqueAlerteUpdateComponent),
    resolve: {
      historiqueAlerte: HistoriqueAlerteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default historiqueAlerteRoute;
