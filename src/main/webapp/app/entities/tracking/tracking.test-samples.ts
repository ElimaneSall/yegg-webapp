import dayjs from 'dayjs/esm';

import { ITracking, NewTracking } from './tracking.model';

export const sampleWithRequiredData: ITracking = {
  id: 15727,
  latitude: 22517.5,
  longitude: 4025.55,
  timestamp: dayjs('2026-02-21T13:27'),
  source: 'ONBOARD_GPS',
};

export const sampleWithPartialData: ITracking = {
  id: 20558,
  latitude: 25958.21,
  longitude: 1702.03,
  vitesse: 26076.89,
  cap: 178,
  precision: 9749,
  timestamp: dayjs('2026-02-21T01:53'),
  source: 'DRIVER_APP',
};

export const sampleWithFullData: ITracking = {
  id: 26336,
  latitude: 10040.82,
  longitude: 2269.52,
  vitesse: 17748.94,
  cap: 195,
  precision: 13420,
  timestamp: dayjs('2026-02-21T13:28'),
  source: 'ONBOARD_GPS',
  evenement: 'émérite',
  commentaire: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewTracking = {
  latitude: 27172.41,
  longitude: 414.21,
  timestamp: dayjs('2026-02-21T01:00'),
  source: 'MANUAL',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
