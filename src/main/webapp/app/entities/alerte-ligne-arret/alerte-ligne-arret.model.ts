import { ILigne } from 'app/entities/ligne/ligne.model';
import { IArret } from 'app/entities/arret/arret.model';
import { IAlerteApproche } from 'app/entities/alerte-approche/alerte-approche.model';

export interface IAlerteLigneArret {
  id: number;
  sens?: string | null;
  actif?: boolean | null;
  ligne?: Pick<ILigne, 'id'> | null;
  arret?: Pick<IArret, 'id'> | null;
  alerteApproche?: Pick<IAlerteApproche, 'id'> | null;
}

export type NewAlerteLigneArret = Omit<IAlerteLigneArret, 'id'> & { id: null };
