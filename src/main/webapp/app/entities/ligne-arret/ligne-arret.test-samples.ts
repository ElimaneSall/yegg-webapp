import { ILigneArret, NewLigneArret } from './ligne-arret.model';

export const sampleWithRequiredData: ILigneArret = {
  id: 7638,
  ordre: 16709,
};

export const sampleWithPartialData: ILigneArret = {
  id: 27825,
  ordre: 7458,
  tempsTrajetDepart: 12241,
};

export const sampleWithFullData: ILigneArret = {
  id: 8992,
  ordre: 16437,
  tempsTrajetDepart: 2433,
  distanceDepart: 24926.42,
};

export const sampleWithNewData: NewLigneArret = {
  ordre: 32560,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
