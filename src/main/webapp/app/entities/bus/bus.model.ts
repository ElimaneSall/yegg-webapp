import dayjs from 'dayjs/esm';
import { ILigne } from 'app/entities/ligne/ligne.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { EnergyType } from 'app/entities/enumerations/energy-type.model';
import { BusStatus } from 'app/entities/enumerations/bus-status.model';

export interface IBus {
  id: number;
  numeroVehicule?: string | null;
  plaque?: string | null;
  modele?: string | null;
  constructeur?: string | null;
  capacite?: number | null;
  capaciteDebout?: number | null;
  anneeFabrication?: number | null;
  energie?: keyof typeof EnergyType | null;
  autonomieKm?: number | null;
  gpsDeviceId?: string | null;
  gpsStatus?: string | null;
  gpsLastPing?: dayjs.Dayjs | null;
  gpsBatteryLevel?: number | null;
  currentLatitude?: number | null;
  currentLongitude?: number | null;
  currentVitesse?: number | null;
  currentCap?: number | null;
  positionUpdatedAt?: dayjs.Dayjs | null;
  statut?: keyof typeof BusStatus | null;
  dateMiseEnService?: dayjs.Dayjs | null;
  dateDernierEntretien?: dayjs.Dayjs | null;
  prochainEntretienKm?: number | null;
  ligne?: Pick<ILigne, 'id'> | null;
  chauffeur?: Pick<IUtilisateur, 'id'> | null;
}

export type NewBus = Omit<IBus, 'id'> & { id: null };
