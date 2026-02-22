import dayjs from 'dayjs/esm';

import { ITracking, NewTracking } from './tracking.model';

export const sampleWithRequiredData: ITracking = {
  id: 15727,
  latitude: 22517.5,
  longitude: 4025.55,
  timestamp: dayjs('2026-02-21T13:27'),
};

export const sampleWithPartialData: ITracking = {
  id: 23356,
  latitude: 26349.62,
  longitude: 20557.4,
  vitesse: 25958.21,
  cap: 18,
  timestamp: dayjs('2026-02-21T18:41'),
  source: 'quant à afin que',
};

export const sampleWithFullData: ITracking = {
  id: 26336,
  latitude: 10040.82,
  longitude: 2269.52,
  vitesse: 17748.94,
  cap: 195,
  timestamp: dayjs('2026-02-21T09:25'),
  source: 'sale remonter',
};

export const sampleWithNewData: NewTracking = {
  latitude: 27172.41,
  longitude: 414.21,
  timestamp: dayjs('2026-02-21T01:00'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
