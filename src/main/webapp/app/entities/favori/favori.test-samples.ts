import dayjs from 'dayjs/esm';

import { IFavori, NewFavori } from './favori.model';

export const sampleWithRequiredData: IFavori = {
  id: 23848,
  type: 'STOP',
  cibleId: 9660,
  dateAjout: dayjs('2026-02-21T17:16'),
};

export const sampleWithPartialData: IFavori = {
  id: 22926,
  type: 'STOP',
  cibleId: 3643,
  dateAjout: dayjs('2026-02-21T05:44'),
};

export const sampleWithFullData: IFavori = {
  id: 11927,
  type: 'STOP',
  cibleId: 3081,
  nomPersonnalise: 'parmi',
  ordre: 21411,
  dateAjout: dayjs('2026-02-21T08:06'),
  dernierAcces: dayjs('2026-02-21T21:21'),
};

export const sampleWithNewData: NewFavori = {
  type: 'STOP',
  cibleId: 734,
  dateAjout: dayjs('2026-02-21T01:50'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
