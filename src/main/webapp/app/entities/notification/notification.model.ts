import dayjs from 'dayjs/esm';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface INotification {
  id: number;
  type?: string | null;
  titre?: string | null;
  message?: string | null;
  donnees?: string | null;
  priorite?: string | null;
  statut?: string | null;
  lu?: boolean | null;
  dateLecture?: dayjs.Dayjs | null;
  utilisateur?: Pick<IUtilisateur, 'id'> | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
