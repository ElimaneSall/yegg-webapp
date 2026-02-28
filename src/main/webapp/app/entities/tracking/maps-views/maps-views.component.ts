import { Component, OnInit, OnDestroy, inject, NgZone, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { BusWebsocketService } from '../service/bus-websocket.service.service';
import { TrackingService } from '../service/tracking.service'; // Vérifiez le chemin
import * as L from 'leaflet';

@Component({
  selector: 'jhi-maps-views',
  standalone: true,
  imports: [CommonModule, FormsModule], // Retrait de GoogleMap car on utilise Leaflet
  templateUrl: './maps-views.component.html',
  styleUrl: './maps-views.component.scss',
})
export class MapsViewsComponent implements OnInit, AfterViewInit, OnDestroy {
  private map!: L.Map;
  private busMarkers = new Map<number, L.Marker>();

  private busWebsocketService = inject(BusWebsocketService);
  private trackingService = inject(TrackingService);
  private ngZone = inject(NgZone); // Pour s'assurer que Leaflet s'exécute correctement

  private wsSubscription?: Subscription;

  ngOnInit(): void {
    this.initMap();
    this.loadInitialPositions(); // 1. Charger l'existant
    this.setupWebSocket(); // 2. Écouter le futur
  }
  ngAfterViewInit(): void {
    this.initMap(); // On initialise la carte une fois que la vue est prête
    this.loadInitialPositions();
    this.setupWebSocket();
  }

  private initMap(): void {
    // Correction des icônes Leaflet par défaut
    const iconRetinaUrl = 'assets/marker-icon-2x.png';
    const iconUrl = 'assets/marker-icon.png';
    const shadowUrl = 'assets/marker-shadow.png';
    L.Marker.prototype.options.icon = L.icon({
      iconRetinaUrl,
      iconUrl,
      shadowUrl,
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41],
    });

    this.map = L.map('map').setView([14.7167, -17.4677], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
    }).addTo(this.map);
    setTimeout(() => {
      this.map.invalidateSize();
    }, 100);
  }

  private loadInitialPositions(): void {
    this.trackingService.query().subscribe(res => {
      if (res.body) {
        // On utilise un dictionnaire temporaire pour ne garder que le tracking le plus récent par bus
        const latestPositions = new Map<number, any>();

        res.body.forEach(tracking => {
          if (tracking.bus?.id) {
            latestPositions.set(tracking.bus.id, tracking);
          }
        });

        // Maintenant on affiche uniquement ces positions uniques
        latestPositions.forEach(tracking => {
          this.updateBusMarker({
            busId: tracking.bus.id,
            numeroVehicule: tracking.bus?.numeroVehicule,
            latitude: tracking.latitude,
            longitude: tracking.longitude,
            vitesse: tracking.vitesse,
          });
        });
      }
    });
  }

  private setupWebSocket(): void {
    this.wsSubscription = this.busWebsocketService.subscribeToBusPositions().subscribe(bus => {
      this.ngZone.run(() => {
        // On force la zone Angular pour la mise à jour UI
        this.updateBusMarker(bus);
      });
    });
  }

  private updateBusMarker(bus: any): void {
    if (!bus.busId || !bus.latitude || !bus.longitude) return;

    const lat = Number(bus.latitude);
    const lng = Number(bus.longitude);
    const coords: L.LatLngExpression = [lat, lng];

    const existingMarker = this.busMarkers.get(bus.busId);

    if (existingMarker) {
      existingMarker.setLatLng(coords);
    } else {
      const newMarker = L.marker(coords).addTo(this.map);
      newMarker.bindPopup(`
        <strong>Bus: ${bus.numeroVehicule || 'N/A'}</strong><br>
        Vitesse: ${bus.vitesse || 0} km/h
      `);
      this.busMarkers.set(bus.busId, newMarker);
    }
  }

  ngOnDestroy(): void {
    this.wsSubscription?.unsubscribe();
    if (this.map) {
      this.map.remove();
    }
  }
}
