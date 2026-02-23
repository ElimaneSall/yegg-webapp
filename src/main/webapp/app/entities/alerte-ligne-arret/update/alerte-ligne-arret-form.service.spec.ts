import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../alerte-ligne-arret.test-samples';

import { AlerteLigneArretFormService } from './alerte-ligne-arret-form.service';

describe('AlerteLigneArret Form Service', () => {
  let service: AlerteLigneArretFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlerteLigneArretFormService);
  });

  describe('Service methods', () => {
    describe('createAlerteLigneArretFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAlerteLigneArretFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sens: expect.any(Object),
            actif: expect.any(Object),
            ligne: expect.any(Object),
            arret: expect.any(Object),
            alerteApproche: expect.any(Object),
          }),
        );
      });

      it('passing IAlerteLigneArret should create a new form with FormGroup', () => {
        const formGroup = service.createAlerteLigneArretFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sens: expect.any(Object),
            actif: expect.any(Object),
            ligne: expect.any(Object),
            arret: expect.any(Object),
            alerteApproche: expect.any(Object),
          }),
        );
      });
    });

    describe('getAlerteLigneArret', () => {
      it('should return NewAlerteLigneArret for default AlerteLigneArret initial value', () => {
        const formGroup = service.createAlerteLigneArretFormGroup(sampleWithNewData);

        const alerteLigneArret = service.getAlerteLigneArret(formGroup) as any;

        expect(alerteLigneArret).toMatchObject(sampleWithNewData);
      });

      it('should return NewAlerteLigneArret for empty AlerteLigneArret initial value', () => {
        const formGroup = service.createAlerteLigneArretFormGroup();

        const alerteLigneArret = service.getAlerteLigneArret(formGroup) as any;

        expect(alerteLigneArret).toMatchObject({});
      });

      it('should return IAlerteLigneArret', () => {
        const formGroup = service.createAlerteLigneArretFormGroup(sampleWithRequiredData);

        const alerteLigneArret = service.getAlerteLigneArret(formGroup) as any;

        expect(alerteLigneArret).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAlerteLigneArret should not enable id FormControl', () => {
        const formGroup = service.createAlerteLigneArretFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAlerteLigneArret should disable id FormControl', () => {
        const formGroup = service.createAlerteLigneArretFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
