import dayjs from 'dayjs/esm';

import { IBus, NewBus } from './bus.model';

export const sampleWithRequiredData: IBus = {
  id: 3427,
  numeroVehicule: 'même si déjà',
  plaque: 'tant coucher aigre',
  capacite: 11,
  statut: 'avant que',
};

export const sampleWithPartialData: IBus = {
  id: 10530,
  numeroVehicule: 'ailleurs en bas de brouiller',
  plaque: 'condamner mériter grimper',
  capacite: 107,
  gpsLastPing: dayjs('2026-02-21T04:44'),
  currentVitesse: 28529.37,
  currentCap: 304,
  positionUpdatedAt: dayjs('2026-02-21T14:15'),
  statut: 'dès que dense bof',
};

export const sampleWithFullData: IBus = {
  id: 14170,
  numeroVehicule: 'magnifique entre ouah',
  plaque: 'raide de sorte que',
  modele: 'tant que',
  capacite: 40,
  anneeFabrication: 12837,
  gpsDeviceId: 'avant-hier habile',
  gpsStatus: 'jeune',
  gpsLastPing: dayjs('2026-02-21T03:58'),
  gpsBatteryLevel: 77,
  currentLatitude: 3767.01,
  currentLongitude: 12511.71,
  currentVitesse: 9292.41,
  currentCap: 114,
  positionUpdatedAt: dayjs('2026-02-21T01:44'),
  statut: 'équipe de recherche jeune plus',
};

export const sampleWithNewData: NewBus = {
  numeroVehicule: 'ça',
  plaque: 'raide svelte',
  capacite: 32,
  statut: 'mince comme tant que',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
