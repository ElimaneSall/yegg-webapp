import { ILigne } from 'app/entities/ligne/ligne.model';
import { IArret } from 'app/entities/arret/arret.model';

export interface ILigneArret {
  id: number;
  ordre?: number | null;
  tempsTrajetDepart?: number | null;
  distanceDepart?: number | null;
  ligne?: Pick<ILigne, 'id'> | null;
  arret?: Pick<IArret, 'id'> | null;
}

export type NewLigneArret = Omit<ILigneArret, 'id'> & { id: null };
