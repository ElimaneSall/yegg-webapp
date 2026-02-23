import dayjs from 'dayjs/esm';

import { IHistoriqueAlerte, NewHistoriqueAlerte } from './historique-alerte.model';

export const sampleWithRequiredData: IHistoriqueAlerte = {
  id: 29395,
  dateDeclenchement: dayjs('2026-02-22T19:23'),
  notificationEnvoyee: false,
};

export const sampleWithPartialData: IHistoriqueAlerte = {
  id: 12822,
  dateDeclenchement: dayjs('2026-02-22T15:13'),
  busNumero: 'triangulaire',
  tempsReel: 25345,
  notificationEnvoyee: true,
};

export const sampleWithFullData: IHistoriqueAlerte = {
  id: 3092,
  dateDeclenchement: dayjs('2026-02-22T12:38'),
  busNumero: 'sans que autour ha ha',
  distanceReelle: 27356,
  tempsReel: 8400,
  typeDeclenchement: 'OR_BOTH',
  notificationEnvoyee: false,
  dateLecture: dayjs('2026-02-22T05:18'),
};

export const sampleWithNewData: NewHistoriqueAlerte = {
  dateDeclenchement: dayjs('2026-02-22T22:41'),
  notificationEnvoyee: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
