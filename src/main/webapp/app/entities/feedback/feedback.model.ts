import dayjs from 'dayjs/esm';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IFeedback {
  id: number;
  note?: number | null;
  commentaire?: string | null;
  typeObjet?: string | null;
  objetId?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  anonyme?: boolean | null;
  utilisateur?: Pick<IUtilisateur, 'id'> | null;
}

export type NewFeedback = Omit<IFeedback, 'id'> & { id: null };
