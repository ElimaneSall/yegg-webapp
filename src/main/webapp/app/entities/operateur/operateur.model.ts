export interface IOperateur {
  id: number;
  nom?: string | null;
  email?: string | null;
  telephone?: string | null;
  adresse?: string | null;
  logo?: string | null;
  logoContentType?: string | null;
  actif?: boolean | null;
}

export type NewOperateur = Omit<IOperateur, 'id'> & { id: null };
