import dayjs from 'dayjs/esm';
import { IBus } from 'app/entities/bus/bus.model';
import { TrackingSource } from 'app/entities/enumerations/tracking-source.model';

export interface ITracking {
  id: number;
  latitude?: number | null;
  longitude?: number | null;
  vitesse?: number | null;
  cap?: number | null;
  precision?: number | null;
  timestamp?: dayjs.Dayjs | null;
  source?: keyof typeof TrackingSource | null;
  evenement?: string | null;
  commentaire?: string | null;
  bus?: Pick<IBus, 'id' | 'numeroVehicule'> | null;
}

export type NewTracking = Omit<ITracking, 'id'> & { id: null };
