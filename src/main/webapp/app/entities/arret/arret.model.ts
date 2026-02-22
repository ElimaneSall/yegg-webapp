export interface IArret {
  id: number;
  nom?: string | null;
  code?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  adresse?: string | null;
  zoneTarifaire?: string | null;
  equipements?: string | null;
  actif?: boolean | null;
}

export type NewArret = Omit<IArret, 'id'> & { id: null };
