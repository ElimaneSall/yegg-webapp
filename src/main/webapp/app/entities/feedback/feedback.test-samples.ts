import dayjs from 'dayjs/esm';

import { IFeedback, NewFeedback } from './feedback.model';

export const sampleWithRequiredData: IFeedback = {
  id: 16297,
  note: 2,
  typeObjet: 'gestionnaire tsoin-tsoin',
  objetId: 202,
  dateCreation: dayjs('2026-02-22T22:06'),
};

export const sampleWithPartialData: IFeedback = {
  id: 30079,
  note: 4,
  typeObjet: 'hors de',
  objetId: 12803,
  dateCreation: dayjs('2026-02-22T09:05'),
};

export const sampleWithFullData: IFeedback = {
  id: 18312,
  note: 4,
  commentaire: '../fake-data/blob/hipster.txt',
  typeObjet: 'encore',
  objetId: 15712,
  dateCreation: dayjs('2026-02-22T19:41'),
  anonyme: true,
};

export const sampleWithNewData: NewFeedback = {
  note: 2,
  typeObjet: 'commissionnaire pff ferme',
  objetId: 28217,
  dateCreation: dayjs('2026-02-22T05:50'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
