import dayjs from 'dayjs/esm';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { ILigne } from 'app/entities/ligne/ligne.model';

export interface IBus {
  id: number;
  numeroVehicule?: string | null;
  plaque?: string | null;
  modele?: string | null;
  capacite?: number | null;
  anneeFabrication?: number | null;
  gpsDeviceId?: string | null;
  gpsStatus?: string | null;
  gpsLastPing?: dayjs.Dayjs | null;
  gpsBatteryLevel?: number | null;
  currentLatitude?: number | null;
  currentLongitude?: number | null;
  currentVitesse?: number | null;
  currentCap?: number | null;
  positionUpdatedAt?: dayjs.Dayjs | null;
  statut?: string | null;
  utilisateur?: Pick<IUtilisateur, 'id'> | null;
  ligne?: Pick<ILigne, 'id'> | null;
}

export type NewBus = Omit<IBus, 'id'> & { id: null };
