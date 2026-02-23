import dayjs from 'dayjs/esm';
import { UserRole } from 'app/entities/enumerations/user-role.model';

export interface IUtilisateur {
  id: number;
  prenom?: string | null;
  nom?: string | null;
  email?: string | null;
  telephone?: string | null;
  motDePasse?: string | null;
  role?: keyof typeof UserRole | null;
  matricule?: string | null;
  fcmToken?: string | null;
  notificationsPush?: boolean | null;
  notificationsSms?: boolean | null;
  langue?: string | null;
  photo?: string | null;
  photoContentType?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  derniereConnexion?: dayjs.Dayjs | null;
  actif?: boolean | null;
  dateEmbauche?: dayjs.Dayjs | null;
  numeroPermis?: string | null;
}

export type NewUtilisateur = Omit<IUtilisateur, 'id'> & { id: null };
