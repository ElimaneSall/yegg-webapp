import dayjs from 'dayjs/esm';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { ThresholdType } from 'app/entities/enumerations/threshold-type.model';
import { AlertStatus } from 'app/entities/enumerations/alert-status.model';

export interface IAlerteApproche {
  id: number;
  nom?: string | null;
  seuilDistance?: number | null;
  seuilTemps?: number | null;
  typeSeuil?: keyof typeof ThresholdType | null;
  joursActivation?: string | null;
  heureDebut?: string | null;
  heureFin?: string | null;
  statut?: keyof typeof AlertStatus | null;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  dernierDeclenchement?: dayjs.Dayjs | null;
  nombreDeclenchements?: number | null;
  utilisateur?: Pick<IUtilisateur, 'id'> | null;
}

export type NewAlerteApproche = Omit<IAlerteApproche, 'id'> & { id: null };
