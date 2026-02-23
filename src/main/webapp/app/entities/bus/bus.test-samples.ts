import dayjs from 'dayjs/esm';

import { IBus, NewBus } from './bus.model';

export const sampleWithRequiredData: IBus = {
  id: 3427,
  numeroVehicule: 'même si déjà',
  plaque: 'tant coucher aigre',
  capacite: 11,
  statut: 'OUT_OF_SERVICE',
};

export const sampleWithPartialData: IBus = {
  id: 12553,
  numeroVehicule: 'en bas de brouiller dénoncer',
  plaque: 'mériter',
  capacite: 54,
  energie: 'DIESEL',
  gpsLastPing: dayjs('2026-02-21T14:40'),
  gpsBatteryLevel: 17,
  currentLatitude: 24108.45,
  currentLongitude: 7888.56,
  statut: 'BROKEN_DOWN',
};

export const sampleWithFullData: IBus = {
  id: 14170,
  numeroVehicule: 'magnifique entre ouah',
  plaque: 'raide de sorte que',
  modele: 'tant que',
  constructeur: 'grâce à',
  capacite: 38,
  capaciteDebout: 51,
  anneeFabrication: 2029,
  energie: 'ELECTRIC',
  autonomieKm: 27133,
  gpsDeviceId: 'selon dès diplomate',
  gpsStatus: 'sauf à serviable atchoum',
  gpsLastPing: dayjs('2026-02-21T09:16'),
  gpsBatteryLevel: 23,
  currentLatitude: 13032.46,
  currentLongitude: 27819.68,
  currentVitesse: 8183.34,
  currentCap: 117,
  positionUpdatedAt: dayjs('2026-02-21T05:52'),
  statut: 'UNDER_MAINTENANCE',
  dateMiseEnService: dayjs('2026-02-21'),
  dateDernierEntretien: dayjs('2026-02-21'),
  prochainEntretienKm: 4147,
};

export const sampleWithNewData: NewBus = {
  numeroVehicule: 'ça',
  plaque: 'raide svelte',
  capacite: 32,
  statut: 'ON_BREAK',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
