export interface IArret {
  id: number;
  nom?: string | null;
  code?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  altitude?: number | null;
  adresse?: string | null;
  ville?: string | null;
  codePostal?: string | null;
  zoneTarifaire?: string | null;
  equipements?: string | null;
  photo?: string | null;
  photoContentType?: string | null;
  accessiblePMR?: boolean | null;
  actif?: boolean | null;
}

export type NewArret = Omit<IArret, 'id'> & { id: null };
