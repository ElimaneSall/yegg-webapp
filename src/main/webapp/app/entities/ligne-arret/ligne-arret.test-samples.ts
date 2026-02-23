import { ILigneArret, NewLigneArret } from './ligne-arret.model';

export const sampleWithRequiredData: ILigneArret = {
  id: 7638,
  ordre: 16709,
};

export const sampleWithPartialData: ILigneArret = {
  id: 12241,
  ordre: 21643,
  tempsTrajetDepart: 19541,
  arretPhysique: true,
};

export const sampleWithFullData: ILigneArret = {
  id: 8992,
  ordre: 16437,
  tempsTrajetDepart: 2433,
  distanceDepart: 24926.42,
  tempsArretMoyen: 15928,
  arretPhysique: false,
};

export const sampleWithNewData: NewLigneArret = {
  ordre: 32560,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
