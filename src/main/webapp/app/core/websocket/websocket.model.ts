export interface BusPosition {
  busId: number;
  numeroVehicule: string;
  plaque: string;
  latitude: number;
  longitude: number;
  vitesse: number | null;
  cap: number | null;
  timestamp: string;
  statut: string;
}

export interface BusAlert {
  type: string;
  message: string;
  busId: number | null;
  timestamp: string;
}
