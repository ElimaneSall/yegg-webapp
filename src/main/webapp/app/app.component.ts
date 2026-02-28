import { Component, inject } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import dayjs from 'dayjs/esm';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import locale from '@angular/common/locales/fr';
// jhipster-needle-angular-add-module-import JHipster will add new module here

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { fontAwesomeIcons } from './config/font-awesome-icons';
import MainComponent from './layouts/main/main.component';
import { TrackerService } from './core/tracker/tracker.service';
import {
  // Icônes de base
  faChevronRight,
  faChevronDown,
  faChevronLeft,
  faUndo,
  faSearch,
  faTimes,
  faSync,
  faPlus,
  faSort,
  faEye,
  faPencilAlt,
  faExclamationTriangle,
  faUser,
  faUsers,
  faNetworkWired,
  faCircle,
  faUserCircle,

  // Icônes pour les entités
  faBuilding, // Opérateurs
  faRoad, // Lignes
  faMapPin, // Arrêts
  faBus, // Bus
  faMap, // Trackings
  faBell, // Alertes
  faStar, // Favoris
  faEnvelope, // Notifications
  faLink, // Lignes-Arrêts
  faHistory, // Ajoutez celle-ci
  faChartBar,
  // Icônes supplémentaires
  faHome,
  faThList,
  faLock,
  faWrench,
  faSignOutAlt,
  faSignInAlt,
  faUserPlus,
  faFlag,
  faBars,
  faUsersCog,
  faTachometerAlt,
  faHeart,
  faCogs,
  faTasks,
  faBook,
  faAsterisk,
} from '@fortawesome/free-solid-svg-icons';

import {
  faBuilding as farBuilding,
  faBell as farBell,
  faStar as farStar,
  faEnvelope as farEnvelope,
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-app',
  template: '<jhi-main />',
  imports: [
    MainComponent,
    // jhipster-needle-angular-add-module JHipster will add new module here
  ],
})
export default class AppComponent {
  private readonly applicationConfigService = inject(ApplicationConfigService);
  private readonly iconLibrary = inject(FaIconLibrary);
  private readonly trackerService = inject(TrackerService);
  private readonly dpConfig = inject(NgbDatepickerConfig);

  constructor(library: FaIconLibrary) {
    library.addIcons(
      faChevronRight,
      faChevronDown,
      faUndo,
      faSearch,
      faTimes,
      faSync,
      faPlus,
      faSort,
      faEye,
      faPencilAlt,
      faExclamationTriangle,
      faUser,
      faUsers,
      faNetworkWired,
      faChevronLeft,
      faCircle,
      faUserCircle,
      faHistory, // Ajoutez celle-ci
      faChartBar,
      // Icônes pour les entités
      faBuilding, // Opérateurs
      faRoad, // Lignes
      faMapPin, // Arrêts
      faBus, // Bus
      faMap, // Trackings
      faBell, // Alertes
      faStar, // Favoris
      faEnvelope, // Notifications
      faLink, // Lignes-Arrêts

      // Icônes supplémentaires
      faHome,
      faThList,
      faLock,
      faWrench,
      faSignOutAlt,
      faSignInAlt,
      faUserPlus,
      faFlag,
      faBars,
      faUsersCog,
      faTachometerAlt,
      faHeart,
      faCogs,
      faTasks,
      faBook,
      faAsterisk,
      farBuilding,
      farBell,
      farStar,
      farEnvelope,
    );

    this.trackerService.setup();
    this.applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    this.iconLibrary.addIcons(...fontAwesomeIcons);
    this.dpConfig.minDate = { year: dayjs().subtract(100, 'year').year(), month: 1, day: 1 };
  }
}
