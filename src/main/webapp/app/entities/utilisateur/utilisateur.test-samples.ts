import dayjs from 'dayjs/esm';

import { IUtilisateur, NewUtilisateur } from './utilisateur.model';

export const sampleWithRequiredData: IUtilisateur = {
  id: 7635,
};

export const sampleWithPartialData: IUtilisateur = {
  id: 12918,
  langue: 'sous couleur de ouin accorder',
  numeroPermis: "à l'égard de sauf à loufoque",
};

export const sampleWithFullData: IUtilisateur = {
  id: 18250,
  matricule: 'si bien que concernant',
  telephone: '109624879',
  fcmToken: 'fonctionnaire',
  notificationsPush: false,
  langue: 'compliquer adversaire patientèle',
  dateEmbauche: dayjs('2026-02-21'),
  numeroPermis: 'plier coin-coin près de',
};

export const sampleWithNewData: NewUtilisateur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
