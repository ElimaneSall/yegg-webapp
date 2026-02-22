import { ILigne, NewLigne } from './ligne.model';

export const sampleWithRequiredData: ILigne = {
  id: 32715,
  numero: 'si',
  nom: 'délégation à condition que',
  direction: 'collègue',
  statut: 'préparer',
};

export const sampleWithPartialData: ILigne = {
  id: 12315,
  numero: 'aigre luna',
  nom: 'hôte moderne exciter',
  direction: 'commissionnaire',
  distanceKm: 9935.17,
  dureeMoyenne: 8957,
  statut: 'malade quitte à lentement',
};

export const sampleWithFullData: ILigne = {
  id: 8378,
  numero: 'voler tsoi',
  nom: 'direction amorcer conseil d’administration',
  direction: 'ouille',
  couleur: '#EA79bF',
  distanceKm: 20707.14,
  dureeMoyenne: 18629,
  statut: "d'abord pendant que",
};

export const sampleWithNewData: NewLigne = {
  numero: 'coin-coin ',
  nom: 'clientèle demain blablabla',
  direction: 'patientèle au-delà',
  statut: 'au point que outre même si',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
