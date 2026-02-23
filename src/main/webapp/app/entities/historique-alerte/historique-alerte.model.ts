import dayjs from 'dayjs/esm';
import { IBus } from 'app/entities/bus/bus.model';
import { IAlerteApproche } from 'app/entities/alerte-approche/alerte-approche.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { ThresholdType } from 'app/entities/enumerations/threshold-type.model';

export interface IHistoriqueAlerte {
  id: number;
  dateDeclenchement?: dayjs.Dayjs | null;
  busNumero?: string | null;
  distanceReelle?: number | null;
  tempsReel?: number | null;
  typeDeclenchement?: keyof typeof ThresholdType | null;
  notificationEnvoyee?: boolean | null;
  dateLecture?: dayjs.Dayjs | null;
  bus?: Pick<IBus, 'id'> | null;
  alerteApproche?: Pick<IAlerteApproche, 'id'> | null;
  utilisateur?: Pick<IUtilisateur, 'id'> | null;
}

export type NewHistoriqueAlerte = Omit<IHistoriqueAlerte, 'id'> & { id: null };
