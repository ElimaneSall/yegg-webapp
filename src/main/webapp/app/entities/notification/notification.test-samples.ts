import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 10110,
  type: 'BREAKDOWN',
  titre: 'malade',
  message: '../fake-data/blob/hipster.txt',
  statut: 'FAILED',
  dateCreation: dayjs('2026-02-21T04:35'),
};

export const sampleWithPartialData: INotification = {
  id: 2380,
  type: 'BREAKDOWN',
  titre: 'hé cadre',
  message: '../fake-data/blob/hipster.txt',
  statut: 'SENT',
  dateCreation: dayjs('2026-02-21T02:37'),
  dateEnvoi: dayjs('2026-02-21T14:05'),
  dateLecture: dayjs('2026-02-21T19:38'),
};

export const sampleWithFullData: INotification = {
  id: 5787,
  type: 'INCIDENT',
  titre: 'immense bè avare',
  message: '../fake-data/blob/hipster.txt',
  donnees: '../fake-data/blob/hipster.txt',
  priorite: 'MEDIUM',
  statut: 'SENT',
  dateCreation: dayjs('2026-02-21T17:09'),
  dateEnvoi: dayjs('2026-02-21T23:29'),
  lu: false,
  dateLecture: dayjs('2026-02-21T08:01'),
};

export const sampleWithNewData: NewNotification = {
  type: 'INFORMATION',
  titre: 'sus jeune enfant secours',
  message: '../fake-data/blob/hipster.txt',
  statut: 'SENT',
  dateCreation: dayjs('2026-02-21T20:53'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
