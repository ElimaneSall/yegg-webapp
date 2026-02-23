import { TestBed } from '@angular/core/testing';

import { BusTrackingService } from './bus/bus-tracking/bus-tracking.service';

describe('BusTrackingService', () => {
  let service: BusTrackingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BusTrackingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
