import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../arret.test-samples';

import { ArretFormService } from './arret-form.service';

describe('Arret Form Service', () => {
  let service: ArretFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArretFormService);
  });

  describe('Service methods', () => {
    describe('createArretFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createArretFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            code: expect.any(Object),
            latitude: expect.any(Object),
            longitude: expect.any(Object),
            altitude: expect.any(Object),
            adresse: expect.any(Object),
            ville: expect.any(Object),
            codePostal: expect.any(Object),
            zoneTarifaire: expect.any(Object),
            equipements: expect.any(Object),
            photo: expect.any(Object),
            accessiblePMR: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });

      it('passing IArret should create a new form with FormGroup', () => {
        const formGroup = service.createArretFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            code: expect.any(Object),
            latitude: expect.any(Object),
            longitude: expect.any(Object),
            altitude: expect.any(Object),
            adresse: expect.any(Object),
            ville: expect.any(Object),
            codePostal: expect.any(Object),
            zoneTarifaire: expect.any(Object),
            equipements: expect.any(Object),
            photo: expect.any(Object),
            accessiblePMR: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });
    });

    describe('getArret', () => {
      it('should return NewArret for default Arret initial value', () => {
        const formGroup = service.createArretFormGroup(sampleWithNewData);

        const arret = service.getArret(formGroup) as any;

        expect(arret).toMatchObject(sampleWithNewData);
      });

      it('should return NewArret for empty Arret initial value', () => {
        const formGroup = service.createArretFormGroup();

        const arret = service.getArret(formGroup) as any;

        expect(arret).toMatchObject({});
      });

      it('should return IArret', () => {
        const formGroup = service.createArretFormGroup(sampleWithRequiredData);

        const arret = service.getArret(formGroup) as any;

        expect(arret).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IArret should not enable id FormControl', () => {
        const formGroup = service.createArretFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewArret should disable id FormControl', () => {
        const formGroup = service.createArretFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
