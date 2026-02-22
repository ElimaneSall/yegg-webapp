import { IFavori, NewFavori } from './favori.model';

export const sampleWithRequiredData: IFavori = {
  id: 23848,
  type: 'voyager super',
  cibleId: 1750,
};

export const sampleWithPartialData: IFavori = {
  id: 16551,
  type: 'débrouiller',
  cibleId: 6930,
};

export const sampleWithFullData: IFavori = {
  id: 11927,
  type: 'ha secours',
  cibleId: 9422,
  nomPersonnalise: 'psitt coordonner',
  ordre: 19348,
  alerteActive: true,
  alerteSeuil: 12,
};

export const sampleWithNewData: NewFavori = {
  type: 'équipe atchoum',
  cibleId: 11350,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
