import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../favori.test-samples';

import { FavoriFormService } from './favori-form.service';

describe('Favori Form Service', () => {
  let service: FavoriFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FavoriFormService);
  });

  describe('Service methods', () => {
    describe('createFavoriFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFavoriFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            cibleId: expect.any(Object),
            nomPersonnalise: expect.any(Object),
            ordre: expect.any(Object),
            alerteActive: expect.any(Object),
            alerteSeuil: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });

      it('passing IFavori should create a new form with FormGroup', () => {
        const formGroup = service.createFavoriFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            cibleId: expect.any(Object),
            nomPersonnalise: expect.any(Object),
            ordre: expect.any(Object),
            alerteActive: expect.any(Object),
            alerteSeuil: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });
    });

    describe('getFavori', () => {
      it('should return NewFavori for default Favori initial value', () => {
        const formGroup = service.createFavoriFormGroup(sampleWithNewData);

        const favori = service.getFavori(formGroup) as any;

        expect(favori).toMatchObject(sampleWithNewData);
      });

      it('should return NewFavori for empty Favori initial value', () => {
        const formGroup = service.createFavoriFormGroup();

        const favori = service.getFavori(formGroup) as any;

        expect(favori).toMatchObject({});
      });

      it('should return IFavori', () => {
        const formGroup = service.createFavoriFormGroup(sampleWithRequiredData);

        const favori = service.getFavori(formGroup) as any;

        expect(favori).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFavori should not enable id FormControl', () => {
        const formGroup = service.createFavoriFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFavori should disable id FormControl', () => {
        const formGroup = service.createFavoriFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
