import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IFavori {
  id: number;
  type?: string | null;
  cibleId?: number | null;
  nomPersonnalise?: string | null;
  ordre?: number | null;
  alerteActive?: boolean | null;
  alerteSeuil?: number | null;
  utilisateur?: Pick<IUtilisateur, 'id'> | null;
}

export type NewFavori = Omit<IFavori, 'id'> & { id: null };
