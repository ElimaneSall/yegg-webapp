import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../operateur.test-samples';

import { OperateurFormService } from './operateur-form.service';

describe('Operateur Form Service', () => {
  let service: OperateurFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OperateurFormService);
  });

  describe('Service methods', () => {
    describe('createOperateurFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOperateurFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            email: expect.any(Object),
            telephone: expect.any(Object),
            adresse: expect.any(Object),
            logo: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });

      it('passing IOperateur should create a new form with FormGroup', () => {
        const formGroup = service.createOperateurFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            email: expect.any(Object),
            telephone: expect.any(Object),
            adresse: expect.any(Object),
            logo: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });
    });

    describe('getOperateur', () => {
      it('should return NewOperateur for default Operateur initial value', () => {
        const formGroup = service.createOperateurFormGroup(sampleWithNewData);

        const operateur = service.getOperateur(formGroup) as any;

        expect(operateur).toMatchObject(sampleWithNewData);
      });

      it('should return NewOperateur for empty Operateur initial value', () => {
        const formGroup = service.createOperateurFormGroup();

        const operateur = service.getOperateur(formGroup) as any;

        expect(operateur).toMatchObject({});
      });

      it('should return IOperateur', () => {
        const formGroup = service.createOperateurFormGroup(sampleWithRequiredData);

        const operateur = service.getOperateur(formGroup) as any;

        expect(operateur).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOperateur should not enable id FormControl', () => {
        const formGroup = service.createOperateurFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOperateur should disable id FormControl', () => {
        const formGroup = service.createOperateurFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
