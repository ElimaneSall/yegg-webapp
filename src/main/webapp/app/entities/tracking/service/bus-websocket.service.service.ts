import { inject, Injectable, OnDestroy } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

@Injectable({ providedIn: 'root' })
export class BusWebsocketService implements OnDestroy {
  private client: Client | null = null;
  private busPositionSubject = new Subject<any>();
  private authServerProvider = inject(AuthServerProvider);

  subscribeToBusPositions(): Observable<any> {
    if (!this.client) {
      this.connect();
    }
    return this.busPositionSubject.asObservable();
  }

  private connect(): void {
    const authToken = this.authServerProvider.getToken();
    const socketUrl = `http://localhost:8080/websocket/tracker?access_token=${authToken}`;
    this.client = new Client({
      webSocketFactory: () => new SockJS(socketUrl),
      connectHeaders: {
        Authorization: `Bearer ${authToken}`,
      },
      debug: str => console.log(str),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    this.client.onConnect = () => {
      console.log('✅ STOMP connecté');

      this.client?.subscribe('/topic/bus-positions', (message: Message) => {
        console.log('📩 Message reçu:', message.body);
        this.busPositionSubject.next(JSON.parse(message.body));
      });
    };

    this.client.onStompError = frame => {
      console.error('❌ Erreur STOMP:', frame);
    };

    this.client.activate();
  }
  ngOnDestroy(): void {
    if (this.client) {
      this.client.deactivate();
    }
  }
}
