import dayjs from 'dayjs/esm';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IAlerte {
  id: number;
  typeCible?: string | null;
  cibleId?: number | null;
  seuilMinutes?: number | null;
  joursActivation?: string | null;
  heureDebut?: dayjs.Dayjs | null;
  heureFin?: dayjs.Dayjs | null;
  statut?: string | null;
  dernierDeclenchement?: dayjs.Dayjs | null;
  nombreDeclenchements?: number | null;
  utilisateur?: Pick<IUtilisateur, 'id'> | null;
}

export type NewAlerte = Omit<IAlerte, 'id'> & { id: null };
