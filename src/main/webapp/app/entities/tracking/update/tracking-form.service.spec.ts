import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tracking.test-samples';

import { TrackingFormService } from './tracking-form.service';

describe('Tracking Form Service', () => {
  let service: TrackingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrackingFormService);
  });

  describe('Service methods', () => {
    describe('createTrackingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrackingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            latitude: expect.any(Object),
            longitude: expect.any(Object),
            vitesse: expect.any(Object),
            cap: expect.any(Object),
            timestamp: expect.any(Object),
            source: expect.any(Object),
            bus: expect.any(Object),
          }),
        );
      });

      it('passing ITracking should create a new form with FormGroup', () => {
        const formGroup = service.createTrackingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            latitude: expect.any(Object),
            longitude: expect.any(Object),
            vitesse: expect.any(Object),
            cap: expect.any(Object),
            timestamp: expect.any(Object),
            source: expect.any(Object),
            bus: expect.any(Object),
          }),
        );
      });
    });

    describe('getTracking', () => {
      it('should return NewTracking for default Tracking initial value', () => {
        const formGroup = service.createTrackingFormGroup(sampleWithNewData);

        const tracking = service.getTracking(formGroup) as any;

        expect(tracking).toMatchObject(sampleWithNewData);
      });

      it('should return NewTracking for empty Tracking initial value', () => {
        const formGroup = service.createTrackingFormGroup();

        const tracking = service.getTracking(formGroup) as any;

        expect(tracking).toMatchObject({});
      });

      it('should return ITracking', () => {
        const formGroup = service.createTrackingFormGroup(sampleWithRequiredData);

        const tracking = service.getTracking(formGroup) as any;

        expect(tracking).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITracking should not enable id FormControl', () => {
        const formGroup = service.createTrackingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTracking should disable id FormControl', () => {
        const formGroup = service.createTrackingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
