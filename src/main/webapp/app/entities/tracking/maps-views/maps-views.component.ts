import { Component, OnInit, AfterViewInit, OnDestroy, inject, signal, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import * as L from 'leaflet';

// CORRECTION: Chemins d'import relatifs avec @/
import { BusWebsocketService } from '../service/bus-websocket.service.service';
import { TrackingService } from '../service/tracking.service';

// Interface pour les messages WebSocket
interface BusPositionMessage {
  busId: number;
  numeroVehicule?: string;
  plaque?: string;
  latitude: number;
  longitude: number;
  vitesse?: number;
  cap?: number | null;
  timestamp?: string;
  statut?: string;
}

// Interface pour les routes suggérées
interface SuggestedRoute {
  line: string;
  duration: number;
  frequency: number;
  path: L.LatLngExpression[];
}

@Component({
  selector: 'jhi-maps-views',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './maps-views.component.html',
  styleUrls: ['./maps-views.component.scss'],
})
export class MapsViewsComponent implements OnInit, AfterViewInit, OnDestroy {
  // Services
  private busWebsocketService = inject(BusWebsocketService);
  private trackingService = inject(TrackingService);
  private ngZone = inject(NgZone);
  private router = inject(Router);

  // Carte et Marqueurs
  private map!: L.Map;
  private busMarkers = new Map<number, L.Marker>();
  private routeMarkers: L.CircleMarker[] = [];
  private currentPolyline?: L.Polyline;
  private wsSubscription?: Subscription;

  // État de la recherche (Citymapper style)
  startStop = '';
  endStop = '';

  // CORRECTION: Utilisation de signal pour suggestedRoutes
  suggestedRoutes = signal<SuggestedRoute[]>([]);
  selectedRoute: SuggestedRoute | null = null;

  ngOnInit(): void {
    // Rien à initialiser ici pour l'instant
  }

  ngAfterViewInit(): void {
    this.initMap();
    this.loadInitialPositions();
    this.setupWebSocket();
  }

  private initMap(): void {
    // CORRECTION: URLs absolues pour les icônes
    const iconDefault = L.icon({
      iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
      shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41],
    });
    L.Marker.prototype.options.icon = iconDefault;

    this.map = L.map('map').setView([14.7167, -17.4677], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
    }).addTo(this.map);

    setTimeout(() => this.map.invalidateSize(), 200);
  }

  // --- LOGIQUE DE RECHERCHE (CITYMAPPER) ---

  searchRoute(): void {
    if (!this.startStop || !this.endStop) return;

    // CORRECTION: Typage explicite des résultats
    const mockResults: SuggestedRoute[] = [
      {
        line: '24',
        duration: 28,
        frequency: 15,
        path: [[14.72, -17.47] as L.LatLngTuple, [14.725, -17.465] as L.LatLngTuple, [14.73, -17.46] as L.LatLngTuple],
      },
      {
        line: 'L1',
        duration: 26,
        frequency: 8,
        path: [[14.72, -17.47] as L.LatLngTuple, [14.715, -17.455] as L.LatLngTuple, [14.71, -17.45] as L.LatLngTuple],
      },
    ];

    this.suggestedRoutes.set(mockResults);
  }

  selectRoute(route: SuggestedRoute): void {
    this.selectedRoute = route;
    this.drawRouteOnMap(route.path);
  }

  private drawRouteOnMap(path: L.LatLngExpression[]): void {
    if (!path || path.length < 2) return;

    if (this.currentPolyline) {
      this.map.removeLayer(this.currentPolyline);
    }

    this.routeMarkers.forEach(m => this.map.removeLayer(m));
    this.routeMarkers = [];

    // Création de la ligne
    this.currentPolyline = L.polyline(path, {
      color: '#28a745',
      weight: 6,
      opacity: 0.7,
    }).addTo(this.map);

    // Ajout des marqueurs de départ et d'arrivée
    const startPoint = path[0];
    const endPoint = path[path.length - 1];

    const start = L.circleMarker(startPoint, {
      color: '#28a745',
      fillColor: '#28a745',
      fillOpacity: 1,
      radius: 7,
      weight: 2,
    }).addTo(this.map);

    const end = L.circleMarker(endPoint, {
      color: '#dc3545',
      fillColor: '#dc3545',
      fillOpacity: 1,
      radius: 7,
      weight: 2,
    }).addTo(this.map);

    this.routeMarkers.push(start, end);
    this.map.fitBounds(this.currentPolyline.getBounds(), { padding: [50, 50] });
  }

  // --- LOGIQUE TRACKING TEMPS RÉEL ---

  private loadInitialPositions(): void {
    this.trackingService.query().subscribe({
      next: res => {
        if (res.body) {
          const latest = new Map<number, any>();
          res.body.forEach(t => {
            if (t.bus?.id) {
              latest.set(t.bus.id, t);
            }
          });
          latest.forEach(t => this.updateBusMarker(this.mapTrackingToBus(t)));
        }
      },
      error: err => console.error('Erreur chargement positions:', err),
    });
  }

  private setupWebSocket(): void {
    // CORRECTION: Vérification que le service existe
    if (this.busWebsocketService) {
      this.wsSubscription = this.busWebsocketService.subscribeToBusPositions().subscribe({
        next: (bus: BusPositionMessage) => {
          this.ngZone.run(() => this.updateBusMarker(bus));
        },
        error: (err: any) => console.error('Erreur WebSocket:', err),
      });
    } else {
      console.warn('WebSocket service non disponible');
    }
  }

  private updateBusMarker(bus: BusPositionMessage): void {
    if (!bus?.busId || !bus?.latitude || !bus?.longitude) return;

    // Protection contre les coordonnées aberrantes
    if (Math.abs(bus.latitude) > 90 || Math.abs(bus.longitude) > 180) {
      console.warn('Coordonnées invalides:', bus);
      return;
    }

    const coords: L.LatLngExpression = [bus.latitude, bus.longitude];
    const marker = this.busMarkers.get(bus.busId);

    if (marker) {
      marker.setLatLng(coords);

      // Mise à jour du popup si nécessaire
      const popup = marker.getPopup();
      if (popup) {
        popup.setContent(this.createPopupContent(bus));
      }
    } else {
      const newMarker = L.marker(coords, {
        icon: this.getBusIcon(bus.statut),
      }).addTo(this.map);

      // Popup avec bouton Alerte
      const popupContent = this.createPopupContent(bus);
      newMarker.bindPopup(popupContent);

      newMarker.on('popupopen', () => {
        setTimeout(() => {
          const btn = document.getElementById(`btn-alert-${bus.busId}`);
          if (btn) {
            btn.addEventListener('click', () => {
              this.router.navigate(['/alerte-approche/new'], {
                queryParams: { busId: bus.busId },
              });
            });
          }
        }, 100);
      });

      this.busMarkers.set(bus.busId, newMarker);
    }
  }

  private createPopupContent(bus: BusPositionMessage): string {
    return `
      <div style="text-align: center; min-width: 150px;">
        <strong style="font-size: 16px;">Bus ${bus.numeroVehicule || bus.busId}</strong><br>
        <span style="color: #666;">${bus.vitesse ? bus.vitesse + ' km/h' : "À l'arrêt"}</span><br>
        <span style="color: #999; font-size: 12px;">${bus.statut || 'En service'}</span><br>
        <button id="btn-alert-${bus.busId}"
                style="background: #ffc107; border: none; padding: 6px 12px;
                       border-radius: 4px; margin-top: 8px; cursor: pointer;">
          Régler une alerte
        </button>
      </div>
    `;
  }

  private getBusIcon(status?: string): L.Icon {
    // CORRECTION: Utilisation d'URLs absolues
    const iconUrl =
      status === 'IN_SERVICE'
        ? 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-green.png'
        : 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-grey.png';

    return L.icon({
      iconUrl: iconUrl,
      shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41],
    });
  }

  private mapTrackingToBus(t: any): BusPositionMessage {
    return {
      busId: t.bus?.id,
      numeroVehicule: t.bus?.numeroVehicule,
      latitude: t.latitude,
      longitude: t.longitude,
      vitesse: t.vitesse,
      statut: t.bus?.statut,
    };
  }

  ngOnDestroy(): void {
    if (this.wsSubscription) {
      this.wsSubscription.unsubscribe();
    }
    if (this.map) {
      this.map.remove();
    }
  }
}
