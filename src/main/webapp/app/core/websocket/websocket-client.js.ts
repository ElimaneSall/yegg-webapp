// src/main/webapp/app/core/websocket/websocket.service.ts

import { Injectable, OnDestroy } from '@angular/core';
import { Observable, Subject, Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RxStomp, RxStompConfig } from '@stomp/rx-stomp';
import SockJS from 'sockjs-client';
import { BusPosition, BusAlert } from './websocket.model';

@Injectable({ providedIn: 'root' })
export class WebSocketService implements OnDestroy {
  private stompClient: RxStomp | null = null;
  private connectionSubject = new Subject<boolean>();
  private positionSubject = new Subject<BusPosition>();
  private alertSubject = new Subject<BusAlert>();

  public connection$ = this.connectionSubject.asObservable();
  public position$ = this.positionSubject.asObservable();
  public alert$ = this.alertSubject.asObservable();

  // Cache des dernières positions
  private positionsCache = new Map<number, BusPosition>();

  constructor() {}

  /**
   * Établit la connexion WebSocket
   */
  connect(): void {
    if (this.stompClient?.connected) {
      return;
    }

    const config = new RxStompConfig();
    config.webSocketFactory = () => {
      return new SockJS('/websocket/tracker');
    };

    config.heartbeatIncoming = 0;
    config.heartbeatOutgoing = 20000;
    config.reconnectDelay = 5000;
    config.debug = (msg: string): void => {
      console.debug(new Date(), msg);
    };

    this.stompClient = new RxStomp();
    this.stompClient.configure(config);
    this.stompClient.activate();

    this.stompClient.connected$.subscribe(() => {
      console.log('✅ WebSocket connecté');
      this.connectionSubject.next(true);
      this.subscribeToTopics();
    });

    this.stompClient.connectionState$.subscribe(state => {
      console.log('📡 État WebSocket:', state);
    });
  }

  /**
   * S'abonne aux topics
   */
  private subscribeToTopics(): void {
    if (!this.stompClient) return;

    // Positions des bus
    this.stompClient.watch('/topic/bus-positions').subscribe(message => {
      try {
        const position: BusPosition = JSON.parse(message.body);
        this.handleBusPosition(position);
      } catch (e) {
        console.error('Erreur parsing position:', e);
      }
    });

    // Alertes
    this.stompClient.watch('/topic/bus-alerts').subscribe(message => {
      try {
        const alert: BusAlert = JSON.parse(message.body);
        this.handleAlert(alert);
      } catch (e) {
        console.error('Erreur parsing alerte:', e);
      }
    });
  }

  /**
   * Gère une nouvelle position de bus
   */
  private handleBusPosition(position: BusPosition): void {
    // Mettre à jour le cache
    this.positionsCache.set(position.busId, position);

    // Émettre l'événement
    this.positionSubject.next(position);

    console.log(`🚌 Bus ${position.numeroVehicule}: (${position.latitude}, ${position.longitude})`);
  }

  /**
   * Gère une alerte
   */
  private handleAlert(alert: BusAlert): void {
    console.log(`⚠️ Alerte: ${alert.type} - ${alert.message}`);
    this.alertSubject.next(alert);
  }

  /**
   * Récupère toutes les positions en cache
   */
  getAllPositions(): BusPosition[] {
    return Array.from(this.positionsCache.values());
  }

  /**
   * Récupère la position d'un bus spécifique
   */
  getBusPosition(busId: number): BusPosition | undefined {
    return this.positionsCache.get(busId);
  }

  /**
   * Se déconnecte
   */
  disconnect(): void {
    if (this.stompClient) {
      this.stompClient.deactivate();
      this.connectionSubject.next(false);
      console.log('🔌 WebSocket déconnecté');
    }
  }

  ngOnDestroy(): void {
    this.disconnect();
  }
}
