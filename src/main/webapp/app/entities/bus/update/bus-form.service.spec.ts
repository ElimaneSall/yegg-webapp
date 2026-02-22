import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../bus.test-samples';

import { BusFormService } from './bus-form.service';

describe('Bus Form Service', () => {
  let service: BusFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BusFormService);
  });

  describe('Service methods', () => {
    describe('createBusFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBusFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroVehicule: expect.any(Object),
            plaque: expect.any(Object),
            modele: expect.any(Object),
            capacite: expect.any(Object),
            anneeFabrication: expect.any(Object),
            gpsDeviceId: expect.any(Object),
            gpsStatus: expect.any(Object),
            gpsLastPing: expect.any(Object),
            gpsBatteryLevel: expect.any(Object),
            currentLatitude: expect.any(Object),
            currentLongitude: expect.any(Object),
            currentVitesse: expect.any(Object),
            currentCap: expect.any(Object),
            positionUpdatedAt: expect.any(Object),
            statut: expect.any(Object),
            utilisateur: expect.any(Object),
            ligne: expect.any(Object),
          }),
        );
      });

      it('passing IBus should create a new form with FormGroup', () => {
        const formGroup = service.createBusFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroVehicule: expect.any(Object),
            plaque: expect.any(Object),
            modele: expect.any(Object),
            capacite: expect.any(Object),
            anneeFabrication: expect.any(Object),
            gpsDeviceId: expect.any(Object),
            gpsStatus: expect.any(Object),
            gpsLastPing: expect.any(Object),
            gpsBatteryLevel: expect.any(Object),
            currentLatitude: expect.any(Object),
            currentLongitude: expect.any(Object),
            currentVitesse: expect.any(Object),
            currentCap: expect.any(Object),
            positionUpdatedAt: expect.any(Object),
            statut: expect.any(Object),
            utilisateur: expect.any(Object),
            ligne: expect.any(Object),
          }),
        );
      });
    });

    describe('getBus', () => {
      it('should return NewBus for default Bus initial value', () => {
        const formGroup = service.createBusFormGroup(sampleWithNewData);

        const bus = service.getBus(formGroup) as any;

        expect(bus).toMatchObject(sampleWithNewData);
      });

      it('should return NewBus for empty Bus initial value', () => {
        const formGroup = service.createBusFormGroup();

        const bus = service.getBus(formGroup) as any;

        expect(bus).toMatchObject({});
      });

      it('should return IBus', () => {
        const formGroup = service.createBusFormGroup(sampleWithRequiredData);

        const bus = service.getBus(formGroup) as any;

        expect(bus).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBus should not enable id FormControl', () => {
        const formGroup = service.createBusFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBus should disable id FormControl', () => {
        const formGroup = service.createBusFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
