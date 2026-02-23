import dayjs from 'dayjs/esm';
import { IOperateur } from 'app/entities/operateur/operateur.model';
import { LineStatus } from 'app/entities/enumerations/line-status.model';

export interface ILigne {
  id: number;
  numero?: string | null;
  nom?: string | null;
  direction?: string | null;
  description?: string | null;
  couleur?: string | null;
  distanceKm?: number | null;
  dureeMoyenne?: number | null;
  frequence?: number | null;
  statut?: keyof typeof LineStatus | null;
  joursFeries?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  actif?: boolean | null;
  operateur?: Pick<IOperateur, 'id'> | null;
}

export type NewLigne = Omit<ILigne, 'id'> & { id: null };
