import dayjs from 'dayjs/esm';

import { IRapport, NewRapport } from './rapport.model';

export const sampleWithRequiredData: IRapport = {
  id: 28108,
  nom: 'conseil d’administration tant que',
  type: 'WEEKLY',
  periodeDebut: dayjs('2026-02-22'),
  periodeFin: dayjs('2026-02-22'),
  dateGeneration: dayjs('2026-02-22T08:04'),
};

export const sampleWithPartialData: IRapport = {
  id: 12777,
  nom: 'jamais à bas de',
  type: 'CUSTOM',
  periodeDebut: dayjs('2026-02-22'),
  periodeFin: dayjs('2026-02-22'),
  contenu: '../fake-data/blob/hipster.txt',
  dateGeneration: dayjs('2026-02-22T20:14'),
};

export const sampleWithFullData: IRapport = {
  id: 27639,
  nom: 'étrangler grandement',
  type: 'CUSTOM',
  periodeDebut: dayjs('2026-02-22'),
  periodeFin: dayjs('2026-02-22'),
  format: 'PDF',
  contenu: '../fake-data/blob/hipster.txt',
  dateGeneration: dayjs('2026-02-22T15:45'),
  generePar: 'toujours tant que plouf',
};

export const sampleWithNewData: NewRapport = {
  nom: 'après que assassiner',
  type: 'WEEKLY',
  periodeDebut: dayjs('2026-02-22'),
  periodeFin: dayjs('2026-02-22'),
  dateGeneration: dayjs('2026-02-22T01:57'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
