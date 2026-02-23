import dayjs from 'dayjs/esm';

import { IAlerteApproche, NewAlerteApproche } from './alerte-approche.model';

export const sampleWithRequiredData: IAlerteApproche = {
  id: 12976,
  typeSeuil: 'AND_BOTH',
  statut: 'PAUSED',
  dateCreation: dayjs('2026-02-22T17:12'),
};

export const sampleWithPartialData: IAlerteApproche = {
  id: 23588,
  typeSeuil: 'DISTANCE',
  joursActivation: 'dense plaider dès que',
  heureFin: 'tandis que',
  statut: 'PAUSED',
  dateCreation: dayjs('2026-02-22T05:18'),
  dernierDeclenchement: dayjs('2026-02-22T07:07'),
};

export const sampleWithFullData: IAlerteApproche = {
  id: 20361,
  nom: 'dessiner',
  seuilDistance: 431,
  seuilTemps: 9,
  typeSeuil: 'OR_BOTH',
  joursActivation: 'frémir annoncer équipe de recherche',
  heureDebut: 'rédaction',
  heureFin: 'chef',
  statut: 'PAUSED',
  dateCreation: dayjs('2026-02-22T13:37'),
  dateModification: dayjs('2026-02-22T09:52'),
  dernierDeclenchement: dayjs('2026-02-22T04:16'),
  nombreDeclenchements: 12036,
};

export const sampleWithNewData: NewAlerteApproche = {
  typeSeuil: 'OR_BOTH',
  statut: 'DISABLED',
  dateCreation: dayjs('2026-02-22T06:54'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
