import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ligne.test-samples';

import { LigneFormService } from './ligne-form.service';

describe('Ligne Form Service', () => {
  let service: LigneFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LigneFormService);
  });

  describe('Service methods', () => {
    describe('createLigneFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLigneFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numero: expect.any(Object),
            nom: expect.any(Object),
            direction: expect.any(Object),
            couleur: expect.any(Object),
            distanceKm: expect.any(Object),
            dureeMoyenne: expect.any(Object),
            statut: expect.any(Object),
            operateur: expect.any(Object),
          }),
        );
      });

      it('passing ILigne should create a new form with FormGroup', () => {
        const formGroup = service.createLigneFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numero: expect.any(Object),
            nom: expect.any(Object),
            direction: expect.any(Object),
            couleur: expect.any(Object),
            distanceKm: expect.any(Object),
            dureeMoyenne: expect.any(Object),
            statut: expect.any(Object),
            operateur: expect.any(Object),
          }),
        );
      });
    });

    describe('getLigne', () => {
      it('should return NewLigne for default Ligne initial value', () => {
        const formGroup = service.createLigneFormGroup(sampleWithNewData);

        const ligne = service.getLigne(formGroup) as any;

        expect(ligne).toMatchObject(sampleWithNewData);
      });

      it('should return NewLigne for empty Ligne initial value', () => {
        const formGroup = service.createLigneFormGroup();

        const ligne = service.getLigne(formGroup) as any;

        expect(ligne).toMatchObject({});
      });

      it('should return ILigne', () => {
        const formGroup = service.createLigneFormGroup(sampleWithRequiredData);

        const ligne = service.getLigne(formGroup) as any;

        expect(ligne).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILigne should not enable id FormControl', () => {
        const formGroup = service.createLigneFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLigne should disable id FormControl', () => {
        const formGroup = service.createLigneFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
