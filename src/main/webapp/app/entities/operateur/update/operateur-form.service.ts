import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOperateur, NewOperateur } from '../operateur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOperateur for edit and NewOperateurFormGroupInput for create.
 */
type OperateurFormGroupInput = IOperateur | PartialWithRequiredKeyOf<NewOperateur>;

type OperateurFormDefaults = Pick<NewOperateur, 'id' | 'actif'>;

type OperateurFormGroupContent = {
  id: FormControl<IOperateur['id'] | NewOperateur['id']>;
  nom: FormControl<IOperateur['nom']>;
  email: FormControl<IOperateur['email']>;
  telephone: FormControl<IOperateur['telephone']>;
  adresse: FormControl<IOperateur['adresse']>;
  logo: FormControl<IOperateur['logo']>;
  logoContentType: FormControl<IOperateur['logoContentType']>;
  actif: FormControl<IOperateur['actif']>;
};

export type OperateurFormGroup = FormGroup<OperateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OperateurFormService {
  createOperateurFormGroup(operateur: OperateurFormGroupInput = { id: null }): OperateurFormGroup {
    const operateurRawValue = {
      ...this.getFormDefaults(),
      ...operateur,
    };
    return new FormGroup<OperateurFormGroupContent>({
      id: new FormControl(
        { value: operateurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(operateurRawValue.nom, {
        validators: [Validators.required],
      }),
      email: new FormControl(operateurRawValue.email, {
        validators: [Validators.required],
      }),
      telephone: new FormControl(operateurRawValue.telephone, {
        validators: [Validators.pattern('^[0-9]{9,15}$')],
      }),
      adresse: new FormControl(operateurRawValue.adresse),
      logo: new FormControl(operateurRawValue.logo),
      logoContentType: new FormControl(operateurRawValue.logoContentType),
      actif: new FormControl(operateurRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getOperateur(form: OperateurFormGroup): IOperateur | NewOperateur {
    return form.getRawValue() as IOperateur | NewOperateur;
  }

  resetForm(form: OperateurFormGroup, operateur: OperateurFormGroupInput): void {
    const operateurRawValue = { ...this.getFormDefaults(), ...operateur };
    form.reset(
      {
        ...operateurRawValue,
        id: { value: operateurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OperateurFormDefaults {
    return {
      id: null,
      actif: false,
    };
  }
}
