import dayjs from 'dayjs/esm';

export interface IUtilisateur {
  id: number;
  matricule?: string | null;
  telephone?: string | null;
  fcmToken?: string | null;
  notificationsPush?: boolean | null;
  langue?: string | null;
  dateEmbauche?: dayjs.Dayjs | null;
  numeroPermis?: string | null;
}

export type NewUtilisateur = Omit<IUtilisateur, 'id'> & { id: null };
