import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 10110,
  titre: 'ramper triathlète',
  message: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: INotification = {
  id: 22441,
  titre: 'hé cadre',
  message: '../fake-data/blob/hipster.txt',
  priorite: 'au cas où',
  lu: false,
  dateLecture: dayjs('2026-02-21T15:39'),
};

export const sampleWithFullData: INotification = {
  id: 5787,
  type: 'sédentaire émérite',
  titre: 'avare',
  message: '../fake-data/blob/hipster.txt',
  donnees: '../fake-data/blob/hipster.txt',
  priorite: 'aigre excuser',
  statut: 'au prix de attacher',
  lu: false,
  dateLecture: dayjs('2026-02-21T06:13'),
};

export const sampleWithNewData: NewNotification = {
  titre: 'de peur que en vérité coin-coin',
  message: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
