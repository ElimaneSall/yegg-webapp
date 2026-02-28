// src/main/webapp/app/entities/entity-navbar-items.ts
import NavbarItem from 'app/layouts/navbar/navbar-item.model';
import {
  faBuilding,
  faRoad,
  faMapPin,
  faBus,
  faMap,
  faUsers,
  faBell,
  faHistory,
  faEnvelope,
  faStar,
  faLink,
  faComment,
  faFileAlt,
  faChartBar,
  faClock,
  faExclamationTriangle,
  faRoute,
  faUserCog,
} from '@fortawesome/free-solid-svg-icons';

export const EntityNavbarItems: NavbarItem[] = [
  // Réseau
  {
    name: 'Opérateur',
    route: '/operateur',
    translationKey: 'global.menu.entities.operateur',
    icon: faBuilding,
  },
  {
    name: 'Ligne',
    route: '/ligne',
    translationKey: 'global.menu.entities.ligne',
    icon: faRoad,
  },
  {
    name: 'Arrêt',
    route: '/arret',
    translationKey: 'global.menu.entities.arret',
    icon: faMapPin,
  },
  {
    name: 'LigneArret',
    route: '/ligne-arret',
    translationKey: 'global.menu.entities.ligneArret',
    icon: faLink,
  },

  // Véhicules
  {
    name: 'Bus',
    route: '/bus',
    translationKey: 'global.menu.entities.bus',
    icon: faBus,
  },
  {
    name: 'Tracking',
    route: '/tracking',
    translationKey: 'global.menu.entities.tracking',
    icon: faMap,
  },
  {
    name: 'MapsViews',
    route: '/tracking/maps-views',
    translationKey: 'global.menu.entities.tracking',
    icon: faMap,
  },

  // Utilisateurs
  {
    name: 'Utilisateur',
    route: '/utilisateur',
    translationKey: 'global.menu.entities.utilisateur',
    icon: faUsers,
  },
  {
    name: 'Favori',
    route: '/favori',
    translationKey: 'global.menu.entities.favori',
    icon: faStar,
  },

  // Alertes
  {
    name: 'AlerteApproche',
    route: '/alerte-approche',
    translationKey: 'global.menu.entities.alerteApproche',
    icon: faBell,
  },
  {
    name: 'AlerteLigneArret',
    route: '/alerte-ligne-arret',
    translationKey: 'global.menu.entities.alerteLigneArret',
    icon: faBell,
  },
  {
    name: 'HistoriqueAlerte',
    route: '/historique-alerte',
    translationKey: 'global.menu.entities.historiqueAlerte',
    icon: faHistory,
  },
  {
    name: 'Notification',
    route: '/notification',
    translationKey: 'global.menu.entities.notification',
    icon: faEnvelope,
  },

  // Feedback & Rapports
  {
    name: 'Feedback',
    route: '/feedback',
    translationKey: 'global.menu.entities.feedback',
    icon: faComment,
  },
  {
    name: 'Rapport',
    route: '/rapport',
    translationKey: 'global.menu.entities.rapport',
    icon: faFileAlt,
  },
];
