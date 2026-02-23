import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ligne-arret.test-samples';

import { LigneArretFormService } from './ligne-arret-form.service';

describe('LigneArret Form Service', () => {
  let service: LigneArretFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LigneArretFormService);
  });

  describe('Service methods', () => {
    describe('createLigneArretFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLigneArretFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ordre: expect.any(Object),
            tempsTrajetDepart: expect.any(Object),
            distanceDepart: expect.any(Object),
            tempsArretMoyen: expect.any(Object),
            arretPhysique: expect.any(Object),
            ligne: expect.any(Object),
            arret: expect.any(Object),
          }),
        );
      });

      it('passing ILigneArret should create a new form with FormGroup', () => {
        const formGroup = service.createLigneArretFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ordre: expect.any(Object),
            tempsTrajetDepart: expect.any(Object),
            distanceDepart: expect.any(Object),
            tempsArretMoyen: expect.any(Object),
            arretPhysique: expect.any(Object),
            ligne: expect.any(Object),
            arret: expect.any(Object),
          }),
        );
      });
    });

    describe('getLigneArret', () => {
      it('should return NewLigneArret for default LigneArret initial value', () => {
        const formGroup = service.createLigneArretFormGroup(sampleWithNewData);

        const ligneArret = service.getLigneArret(formGroup) as any;

        expect(ligneArret).toMatchObject(sampleWithNewData);
      });

      it('should return NewLigneArret for empty LigneArret initial value', () => {
        const formGroup = service.createLigneArretFormGroup();

        const ligneArret = service.getLigneArret(formGroup) as any;

        expect(ligneArret).toMatchObject({});
      });

      it('should return ILigneArret', () => {
        const formGroup = service.createLigneArretFormGroup(sampleWithRequiredData);

        const ligneArret = service.getLigneArret(formGroup) as any;

        expect(ligneArret).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILigneArret should not enable id FormControl', () => {
        const formGroup = service.createLigneArretFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLigneArret should disable id FormControl', () => {
        const formGroup = service.createLigneArretFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
