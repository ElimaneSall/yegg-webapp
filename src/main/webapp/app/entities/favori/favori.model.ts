import dayjs from 'dayjs/esm';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { FavoriteType } from 'app/entities/enumerations/favorite-type.model';

export interface IFavori {
  id: number;
  type?: keyof typeof FavoriteType | null;
  cibleId?: number | null;
  nomPersonnalise?: string | null;
  ordre?: number | null;
  dateAjout?: dayjs.Dayjs | null;
  dernierAcces?: dayjs.Dayjs | null;
  utilisateur?: Pick<IUtilisateur, 'id'> | null;
}

export type NewFavori = Omit<IFavori, 'id'> & { id: null };
