import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Opérateurs',
    route: '/operateur',
    translationKey: 'global.menu.entities.operateur',
    icon: 'building', // Maintenant disponible avec l'import ci-dessus
  },
  {
    name: 'Lignes',
    route: '/ligne',
    translationKey: 'global.menu.entities.ligne',
    icon: 'road', // Maintenant disponible
  },
  {
    name: 'Arrêts',
    route: '/arret',
    translationKey: 'global.menu.entities.arret',
    icon: 'map-pin', // Maintenant disponible
  },
  {
    name: 'Bus',
    route: '/bus',
    translationKey: 'global.menu.entities.bus',
    icon: 'bus', // Maintenant disponible
  },
  {
    name: 'Utilisateurs',
    route: '/utilisateur',
    translationKey: 'global.menu.entities.utilisateur',
    icon: 'users', // Maintenant disponible
  },
  {
    name: 'Trackings',
    route: '/tracking',
    translationKey: 'global.menu.entities.tracking',
    icon: 'map', // Maintenant disponible
  },
  {
    name: 'Alertes',
    route: '/alerte',
    translationKey: 'global.menu.entities.alerte',
    icon: 'bell', // Maintenant disponible
  },
  {
    name: 'Favoris',
    route: '/favori',
    translationKey: 'global.menu.entities.favori',
    icon: 'star', // Maintenant disponible
  },
  {
    name: 'Notifications',
    route: '/notification',
    translationKey: 'global.menu.entities.notification',
    icon: 'envelope', // Maintenant disponible
  },
  {
    name: 'Lignes-Arrêts',
    route: '/ligne-arret',
    translationKey: 'global.menu.entities.ligneArret',
    icon: 'link', // Maintenant disponible
  },
];
