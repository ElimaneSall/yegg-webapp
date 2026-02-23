import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'yeggApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'operateur',
    data: { pageTitle: 'yeggApp.operateur.home.title' },
    loadChildren: () => import('./operateur/operateur.routes'),
  },
  {
    path: 'ligne',
    data: { pageTitle: 'yeggApp.ligne.home.title' },
    loadChildren: () => import('./ligne/ligne.routes'),
  },
  {
    path: 'arret',
    data: { pageTitle: 'yeggApp.arret.home.title' },
    loadChildren: () => import('./arret/arret.routes'),
  },
  {
    path: 'bus',
    data: { pageTitle: 'yeggApp.bus.home.title' },
    loadChildren: () => import('./bus/bus.routes'),
  },
  {
    path: 'utilisateur',
    data: { pageTitle: 'yeggApp.utilisateur.home.title' },
    loadChildren: () => import('./utilisateur/utilisateur.routes'),
  },
  {
    path: 'tracking',
    data: { pageTitle: 'yeggApp.tracking.home.title' },
    loadChildren: () => import('./tracking/tracking.routes'),
  },
  {
    path: 'alerte',
    data: { pageTitle: 'yeggApp.alerte.home.title' },
    loadChildren: () => import('./alerte/alerte.routes'),
  },
  {
    path: 'favori',
    data: { pageTitle: 'yeggApp.favori.home.title' },
    loadChildren: () => import('./favori/favori.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'yeggApp.notification.home.title' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  {
    path: 'ligne-arret',
    data: { pageTitle: 'yeggApp.ligneArret.home.title' },
    loadChildren: () => import('./ligne-arret/ligne-arret.routes'),
  },
  {
    path: 'alerte-approche',
    data: { pageTitle: 'yeggApp.alerteApproche.home.title' },
    loadChildren: () => import('./alerte-approche/alerte-approche.routes'),
  },
  {
    path: 'alerte-ligne-arret',
    data: { pageTitle: 'yeggApp.alerteLigneArret.home.title' },
    loadChildren: () => import('./alerte-ligne-arret/alerte-ligne-arret.routes'),
  },
  {
    path: 'historique-alerte',
    data: { pageTitle: 'yeggApp.historiqueAlerte.home.title' },
    loadChildren: () => import('./historique-alerte/historique-alerte.routes'),
  },
  {
    path: 'feedback',
    data: { pageTitle: 'yeggApp.feedback.home.title' },
    loadChildren: () => import('./feedback/feedback.routes'),
  },
  {
    path: 'rapport',
    data: { pageTitle: 'yeggApp.rapport.home.title' },
    loadChildren: () => import('./rapport/rapport.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
