import { IOperateur } from 'app/entities/operateur/operateur.model';

export interface ILigne {
  id: number;
  numero?: string | null;
  nom?: string | null;
  direction?: string | null;
  couleur?: string | null;
  distanceKm?: number | null;
  dureeMoyenne?: number | null;
  statut?: string | null;
  operateur?: Pick<IOperateur, 'id'> | null;
}

export type NewLigne = Omit<ILigne, 'id'> & { id: null };
