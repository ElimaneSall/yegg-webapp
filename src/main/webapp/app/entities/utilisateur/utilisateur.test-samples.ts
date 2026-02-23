import dayjs from 'dayjs/esm';

import { IUtilisateur, NewUtilisateur } from './utilisateur.model';

export const sampleWithRequiredData: IUtilisateur = {
  id: 7635,
  role: 'DRIVER',
  dateCreation: dayjs('2026-02-21T20:00'),
  actif: false,
};

export const sampleWithPartialData: IUtilisateur = {
  id: 18909,
  motDePasse: 'ouin accorder coin-coin',
  role: 'DRIVER',
  fcmToken: 'partenaire large sous',
  notificationsPush: false,
  dateCreation: dayjs('2026-02-21T22:16'),
  derniereConnexion: dayjs('2026-02-21T22:05'),
  actif: true,
  dateEmbauche: dayjs('2026-02-21'),
  numeroPermis: 'meuh dénoncer',
};

export const sampleWithFullData: IUtilisateur = {
  id: 18250,
  prenom: 'si bien que concernant',
  nom: 'résister',
  email: 'Yvonne.Philippe@hotmail.fr',
  telephone: '0400570546',
  motDePasse: 'pourvu que triathlète dans la mesure où',
  role: 'PASSENGER',
  matricule: 'en plus de rectorat jouer',
  fcmToken: 'ouf rédaction',
  notificationsPush: true,
  notificationsSms: true,
  langue: 'exprès',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  dateCreation: dayjs('2026-02-21T18:58'),
  derniereConnexion: dayjs('2026-02-21T11:48'),
  actif: false,
  dateEmbauche: dayjs('2026-02-21'),
  numeroPermis: 'dès que toc-toc',
};

export const sampleWithNewData: NewUtilisateur = {
  role: 'TOURIST',
  dateCreation: dayjs('2026-02-21T20:57'),
  actif: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
