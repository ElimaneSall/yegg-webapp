import dayjs from 'dayjs/esm';

import { IAlerte, NewAlerte } from './alerte.model';

export const sampleWithRequiredData: IAlerte = {
  id: 27320,
  typeCible: 'à demi',
  cibleId: 18865,
  seuilMinutes: 10,
  statut: 'accéder',
};

export const sampleWithPartialData: IAlerte = {
  id: 29655,
  typeCible: 'sauf depuis triangulaire',
  cibleId: 20824,
  seuilMinutes: 18,
  heureFin: dayjs('2026-02-21T13:58'),
  statut: 'au dépens de pourvu que turquoise',
};

export const sampleWithFullData: IAlerte = {
  id: 22369,
  typeCible: 'essuyer alors que probablement',
  cibleId: 25774,
  seuilMinutes: 54,
  joursActivation: 'malgré',
  heureDebut: dayjs('2026-02-21T01:03'),
  heureFin: dayjs('2026-02-21T02:53'),
  statut: 'de peur que parmi croâ',
  dernierDeclenchement: dayjs('2026-02-21T03:28'),
  nombreDeclenchements: 1150,
};

export const sampleWithNewData: NewAlerte = {
  typeCible: 'tic-tac auparavant',
  cibleId: 29849,
  seuilMinutes: 31,
  statut: "d'abord parce que de peur de",
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
