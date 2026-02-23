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
  id: 15009,
  nom: 'pff repousser paf',
  code: 'grâce à si',
  latitude: -68.59,
  longitude: -139.35,
  ville: 'crac',
  codePostal: 'considérable de manière à ce que certes',
  accessiblePMR: false,
  actif: true,
};

export const sampleWithFullData: IArret = {
  id: 6071,
  nom: 'multiple',
  code: 'assez afin de',
  latitude: 64.6,
  longitude: 176.98,
  altitude: 1595,
  adresse: 'partir lectorat',
  ville: 'énergique représenter',
  codePostal: "fonctionnaire à l'insu de au cas où",
  zoneTarifaire: 'broum',
  equipements: '../fake-data/blob/hipster.txt',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  accessiblePMR: false,
  actif: false,
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
