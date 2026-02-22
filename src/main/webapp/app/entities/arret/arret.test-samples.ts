import { IArret, NewArret } from './arret.model';

export const sampleWithRequiredData: IArret = {
  id: 18756,
  nom: 'après que à la merci',
  code: 'hypocrite ouch vérifier',
  latitude: 16.68,
  longitude: 174.78,
  actif: false,
};

export const sampleWithPartialData: IArret = {
  id: 9865,
  nom: 'garder ouch',
  code: 'contre personnel recta',
  latitude: 74,
  longitude: -10.3,
  equipements: '../fake-data/blob/hipster.txt',
  actif: true,
};

export const sampleWithFullData: IArret = {
  id: 6071,
  nom: 'multiple',
  code: 'assez afin de',
  latitude: 64.6,
  longitude: 176.98,
  adresse: 'raisonner',
  zoneTarifaire: 'lectorat équipe de recherche',
  equipements: '../fake-data/blob/hipster.txt',
  actif: true,
};

export const sampleWithNewData: NewArret = {
  nom: 'coac coac',
  code: 'suivre magenta smack',
  latitude: -41.34,
  longitude: 12.58,
  actif: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
