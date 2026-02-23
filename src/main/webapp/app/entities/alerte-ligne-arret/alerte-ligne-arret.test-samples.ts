import { IAlerteLigneArret, NewAlerteLigneArret } from './alerte-ligne-arret.model';

export const sampleWithRequiredData: IAlerteLigneArret = {
  id: 26089,
};

export const sampleWithPartialData: IAlerteLigneArret = {
  id: 21210,
  actif: false,
};

export const sampleWithFullData: IAlerteLigneArret = {
  id: 27632,
  sens: 'susciter',
  actif: false,
};

export const sampleWithNewData: NewAlerteLigneArret = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
