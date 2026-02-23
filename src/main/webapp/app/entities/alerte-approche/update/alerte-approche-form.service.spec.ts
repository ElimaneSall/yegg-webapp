import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../alerte-approche.test-samples';

import { AlerteApprocheFormService } from './alerte-approche-form.service';

describe('AlerteApproche Form Service', () => {
  let service: AlerteApprocheFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlerteApprocheFormService);
  });

  describe('Service methods', () => {
    describe('createAlerteApprocheFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAlerteApprocheFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            seuilDistance: expect.any(Object),
            seuilTemps: expect.any(Object),
            typeSeuil: expect.any(Object),
            joursActivation: expect.any(Object),
            heureDebut: expect.any(Object),
            heureFin: expect.any(Object),
            statut: expect.any(Object),
            dateCreation: expect.any(Object),
            dateModification: expect.any(Object),
            dernierDeclenchement: expect.any(Object),
            nombreDeclenchements: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });

      it('passing IAlerteApproche should create a new form with FormGroup', () => {
        const formGroup = service.createAlerteApprocheFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            seuilDistance: expect.any(Object),
            seuilTemps: expect.any(Object),
            typeSeuil: expect.any(Object),
            joursActivation: expect.any(Object),
            heureDebut: expect.any(Object),
            heureFin: expect.any(Object),
            statut: expect.any(Object),
            dateCreation: expect.any(Object),
            dateModification: expect.any(Object),
            dernierDeclenchement: expect.any(Object),
            nombreDeclenchements: expect.any(Object),
            utilisateur: expect.any(Object),
          }),
        );
      });
    });

    describe('getAlerteApproche', () => {
      it('should return NewAlerteApproche for default AlerteApproche initial value', () => {
        const formGroup = service.createAlerteApprocheFormGroup(sampleWithNewData);

        const alerteApproche = service.getAlerteApproche(formGroup) as any;

        expect(alerteApproche).toMatchObject(sampleWithNewData);
      });

      it('should return NewAlerteApproche for empty AlerteApproche initial value', () => {
        const formGroup = service.createAlerteApprocheFormGroup();

        const alerteApproche = service.getAlerteApproche(formGroup) as any;

        expect(alerteApproche).toMatchObject({});
      });

      it('should return IAlerteApproche', () => {
        const formGroup = service.createAlerteApprocheFormGroup(sampleWithRequiredData);

        const alerteApproche = service.getAlerteApproche(formGroup) as any;

        expect(alerteApproche).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAlerteApproche should not enable id FormControl', () => {
        const formGroup = service.createAlerteApprocheFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAlerteApproche should disable id FormControl', () => {
        const formGroup = service.createAlerteApprocheFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
