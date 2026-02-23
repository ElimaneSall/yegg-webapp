import dayjs from 'dayjs/esm';

import { ILigne, NewLigne } from './ligne.model';

export const sampleWithRequiredData: ILigne = {
  id: 32715,
  numero: 'si',
  nom: 'délégation à condition que',
  direction: 'collègue',
  statut: 'SUSPENDED',
  actif: false,
};

export const sampleWithPartialData: ILigne = {
  id: 27138,
  numero: 'plaisanter',
  nom: 'gigantesque',
  direction: 'pacifique',
  couleur: 'exciter vroum',
  distanceKm: 8184.61,
  dureeMoyenne: 9935,
  statut: 'SUSPENDED',
  joursFeries: '../fake-data/blob/hipster.txt',
  dateDebut: dayjs('2026-02-21'),
  dateFin: dayjs('2026-02-21'),
  actif: true,
};

export const sampleWithFullData: ILigne = {
  id: 8378,
  numero: 'voler tsoi',
  nom: 'direction amorcer conseil d’administration',
  direction: 'ouille',
  description: '../fake-data/blob/hipster.txt',
  couleur: 'mature beaucoup',
  distanceKm: 17492.3,
  dureeMoyenne: 31670,
  frequence: 44,
  statut: 'CLOSED',
  joursFeries: '../fake-data/blob/hipster.txt',
  dateDebut: dayjs('2026-02-21'),
  dateFin: dayjs('2026-02-21'),
  actif: true,
};

export const sampleWithNewData: NewLigne = {
  numero: 'coin-coin ',
  nom: 'clientèle demain blablabla',
  direction: 'patientèle au-delà',
  statut: 'UNDER_CONSTRUCTION',
  actif: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
