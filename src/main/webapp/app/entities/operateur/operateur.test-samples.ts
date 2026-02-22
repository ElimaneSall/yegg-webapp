import { IOperateur, NewOperateur } from './operateur.model';

export const sampleWithRequiredData: IOperateur = {
  id: 1838,
  nom: 'aussi',
  email: 'Alpinien_Moreau18@gmail.com',
  actif: false,
};

export const sampleWithPartialData: IOperateur = {
  id: 11792,
  nom: 'timide glouglou',
  email: 'Coralie_Meunier2@hotmail.fr',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  actif: true,
};

export const sampleWithFullData: IOperateur = {
  id: 24307,
  nom: 'sur à raison de rédaction',
  email: 'Adhemar36@yahoo.fr',
  telephone: '455509272340',
  adresse: '../fake-data/blob/hipster.txt',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  actif: true,
};

export const sampleWithNewData: NewOperateur = {
  nom: 'membre à vie',
  email: 'Doriane_Lopez78@hotmail.fr',
  actif: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
