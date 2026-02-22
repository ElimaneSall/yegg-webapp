import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../alerte.test-samples';

import { AlerteFormService } from './alerte-form.service';

describe('Alerte Form Service', () => {
  let service: AlerteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlerteFormService);
  });

  describe('Service methods', () => {
    describe('createAlerteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAlerteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeCible: expect.any(Object),
            cibleId: expect.any(Object),
            seuilMinutes: expect.any(Object),
            joursActivation: expect.any(Object),
            heureDebut: expect.any(Object),
            heureFin: expect.any(Object),
            statut: expect.any(Object),
            dernierDeclenchement: expect.any(Object),
            nombreDeclenchements: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });

      it('passing IAlerte should create a new form with FormGroup', () => {
        const formGroup = service.createAlerteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeCible: expect.any(Object),
            cibleId: expect.any(Object),
            seuilMinutes: expect.any(Object),
            joursActivation: expect.any(Object),
            heureDebut: expect.any(Object),
            heureFin: expect.any(Object),
            statut: expect.any(Object),
            dernierDeclenchement: expect.any(Object),
            nombreDeclenchements: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });
    });

    describe('getAlerte', () => {
      it('should return NewAlerte for default Alerte initial value', () => {
        const formGroup = service.createAlerteFormGroup(sampleWithNewData);

        const alerte = service.getAlerte(formGroup) as any;

        expect(alerte).toMatchObject(sampleWithNewData);
      });

      it('should return NewAlerte for empty Alerte initial value', () => {
        const formGroup = service.createAlerteFormGroup();

        const alerte = service.getAlerte(formGroup) as any;

        expect(alerte).toMatchObject({});
      });

      it('should return IAlerte', () => {
        const formGroup = service.createAlerteFormGroup(sampleWithRequiredData);

        const alerte = service.getAlerte(formGroup) as any;

        expect(alerte).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAlerte should not enable id FormControl', () => {
        const formGroup = service.createAlerteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAlerte should disable id FormControl', () => {
        const formGroup = service.createAlerteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
