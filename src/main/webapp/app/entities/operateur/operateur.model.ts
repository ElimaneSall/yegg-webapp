import dayjs from 'dayjs/esm';

export interface IOperateur {
  id: number;
  nom?: string | null;
  email?: string | null;
  telephone?: string | null;
  adresse?: string | null;
  logo?: string | null;
  logoContentType?: string | null;
  siteWeb?: string | null;
  siret?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  actif?: boolean | null;
}

export type NewOperateur = Omit<IOperateur, 'id'> & { id: null };
