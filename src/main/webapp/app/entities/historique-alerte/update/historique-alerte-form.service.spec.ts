import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../historique-alerte.test-samples';

import { HistoriqueAlerteFormService } from './historique-alerte-form.service';

describe('HistoriqueAlerte Form Service', () => {
  let service: HistoriqueAlerteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HistoriqueAlerteFormService);
  });

  describe('Service methods', () => {
    describe('createHistoriqueAlerteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHistoriqueAlerteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDeclenchement: expect.any(Object),
            busNumero: expect.any(Object),
            distanceReelle: expect.any(Object),
            tempsReel: expect.any(Object),
            typeDeclenchement: expect.any(Object),
            notificationEnvoyee: expect.any(Object),
            dateLecture: expect.any(Object),
            bus: expect.any(Object),
            alerteApproche: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });

      it('passing IHistoriqueAlerte should create a new form with FormGroup', () => {
        const formGroup = service.createHistoriqueAlerteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDeclenchement: expect.any(Object),
            busNumero: expect.any(Object),
            distanceReelle: expect.any(Object),
            tempsReel: expect.any(Object),
            typeDeclenchement: expect.any(Object),
            notificationEnvoyee: expect.any(Object),
            dateLecture: expect.any(Object),
            bus: expect.any(Object),
            alerteApproche: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });
    });

    describe('getHistoriqueAlerte', () => {
      it('should return NewHistoriqueAlerte for default HistoriqueAlerte initial value', () => {
        const formGroup = service.createHistoriqueAlerteFormGroup(sampleWithNewData);

        const historiqueAlerte = service.getHistoriqueAlerte(formGroup) as any;

        expect(historiqueAlerte).toMatchObject(sampleWithNewData);
      });

      it('should return NewHistoriqueAlerte for empty HistoriqueAlerte initial value', () => {
        const formGroup = service.createHistoriqueAlerteFormGroup();

        const historiqueAlerte = service.getHistoriqueAlerte(formGroup) as any;

        expect(historiqueAlerte).toMatchObject({});
      });

      it('should return IHistoriqueAlerte', () => {
        const formGroup = service.createHistoriqueAlerteFormGroup(sampleWithRequiredData);

        const historiqueAlerte = service.getHistoriqueAlerte(formGroup) as any;

        expect(historiqueAlerte).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHistoriqueAlerte should not enable id FormControl', () => {
        const formGroup = service.createHistoriqueAlerteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHistoriqueAlerte should disable id FormControl', () => {
        const formGroup = service.createHistoriqueAlerteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
