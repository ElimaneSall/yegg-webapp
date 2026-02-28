import { TestBed } from '@angular/core/testing';

import { BusWebsocketServiceService } from './bus-websocket.service.service';

describe('BusWebsocketServiceService', () => {
  let service: BusWebsocketServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BusWebsocketServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
