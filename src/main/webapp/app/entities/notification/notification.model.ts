import dayjs from 'dayjs/esm';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { NotificationType } from 'app/entities/enumerations/notification-type.model';
import { Priority } from 'app/entities/enumerations/priority.model';
import { NotificationStatus } from 'app/entities/enumerations/notification-status.model';

export interface INotification {
  id: number;
  type?: keyof typeof NotificationType | null;
  titre?: string | null;
  message?: string | null;
  donnees?: string | null;
  priorite?: keyof typeof Priority | null;
  statut?: keyof typeof NotificationStatus | null;
  dateCreation?: dayjs.Dayjs | null;
  dateEnvoi?: dayjs.Dayjs | null;
  lu?: boolean | null;
  dateLecture?: dayjs.Dayjs | null;
  utilisateur?: Pick<IUtilisateur, 'id'> | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
