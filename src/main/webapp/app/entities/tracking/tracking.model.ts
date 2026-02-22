import dayjs from 'dayjs/esm';
import { IBus } from 'app/entities/bus/bus.model';

export interface ITracking {
  id: number;
  latitude?: number | null;
  longitude?: number | null;
  vitesse?: number | null;
  cap?: number | null;
  timestamp?: dayjs.Dayjs | null;
  source?: string | null;
  bus?: Pick<IBus, 'id'> | null;
}

export type NewTracking = Omit<ITracking, 'id'> & { id: null };
