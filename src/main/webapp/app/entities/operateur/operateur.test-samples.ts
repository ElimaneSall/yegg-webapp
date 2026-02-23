import dayjs from 'dayjs/esm';

import { IOperateur, NewOperateur } from './operateur.model';

export const sampleWithRequiredData: IOperateur = {
  id: 1838,
  nom: 'aussi',
  email: 'Alpinien_Moreau18@gmail.com',
  dateCreation: dayjs('2026-02-21T18:00'),
  actif: true,
};

export const sampleWithPartialData: IOperateur = {
  id: 5143,
  nom: 'sus',
  email: 'Chilperic.Guerin@gmail.com',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  siteWeb: 'parce que toc-toc déceler',
  dateCreation: dayjs('2026-02-21T13:00'),
  actif: true,
};

export const sampleWithFullData: IOperateur = {
  id: 24307,
  nom: 'sur à raison de rédaction',
  email: 'Adhemar36@yahoo.fr',
  telephone: '+33 145550927',
  adresse: '../fake-data/blob/hipster.txt',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  siteWeb: 'là',
  siret: 'animer guère',
  dateCreation: dayjs('2026-02-21T13:34'),
  actif: true,
};

export const sampleWithNewData: NewOperateur = {
  nom: 'membre à vie',
  email: 'Doriane_Lopez78@hotmail.fr',
  dateCreation: dayjs('2026-02-21T07:54'),
  actif: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
