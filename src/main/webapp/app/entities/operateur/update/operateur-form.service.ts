import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOperateur | NewOperateur> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

type OperateurFormRawValue = FormValueOf<IOperateur>;

type NewOperateurFormRawValue = FormValueOf<NewOperateur>;

type OperateurFormDefaults = Pick<NewOperateur, 'id' | 'dateCreation' | 'actif'>;

type OperateurFormGroupContent = {
  id: FormControl<OperateurFormRawValue['id'] | NewOperateur['id']>;
  nom: FormControl<OperateurFormRawValue['nom']>;
  email: FormControl<OperateurFormRawValue['email']>;
  telephone: FormControl<OperateurFormRawValue['telephone']>;
  adresse: FormControl<OperateurFormRawValue['adresse']>;
  logo: FormControl<OperateurFormRawValue['logo']>;
  logoContentType: FormControl<OperateurFormRawValue['logoContentType']>;
  siteWeb: FormControl<OperateurFormRawValue['siteWeb']>;
  siret: FormControl<OperateurFormRawValue['siret']>;
  dateCreation: FormControl<OperateurFormRawValue['dateCreation']>;
  actif: FormControl<OperateurFormRawValue['actif']>;
};

export type OperateurFormGroup = FormGroup<OperateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OperateurFormService {
  createOperateurFormGroup(operateur: OperateurFormGroupInput = { id: null }): OperateurFormGroup {
    const operateurRawValue = this.convertOperateurToOperateurRawValue({
      ...this.getFormDefaults(),
      ...operateur,
    });
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
      telephone: new FormControl(operateurRawValue.telephone),
      adresse: new FormControl(operateurRawValue.adresse),
      logo: new FormControl(operateurRawValue.logo),
      logoContentType: new FormControl(operateurRawValue.logoContentType),
      siteWeb: new FormControl(operateurRawValue.siteWeb),
      siret: new FormControl(operateurRawValue.siret),
      dateCreation: new FormControl(operateurRawValue.dateCreation, {
        validators: [Validators.required],
      }),
      actif: new FormControl(operateurRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getOperateur(form: OperateurFormGroup): IOperateur | NewOperateur {
    return this.convertOperateurRawValueToOperateur(form.getRawValue() as OperateurFormRawValue | NewOperateurFormRawValue);
  }

  resetForm(form: OperateurFormGroup, operateur: OperateurFormGroupInput): void {
    const operateurRawValue = this.convertOperateurToOperateurRawValue({ ...this.getFormDefaults(), ...operateur });
    form.reset(
      {
        ...operateurRawValue,
        id: { value: operateurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OperateurFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreation: currentTime,
      actif: false,
    };
  }

  private convertOperateurRawValueToOperateur(rawOperateur: OperateurFormRawValue | NewOperateurFormRawValue): IOperateur | NewOperateur {
    return {
      ...rawOperateur,
      dateCreation: dayjs(rawOperateur.dateCreation, DATE_TIME_FORMAT),
    };
  }

  private convertOperateurToOperateurRawValue(
    operateur: IOperateur | (Partial<NewOperateur> & OperateurFormDefaults),
  ): OperateurFormRawValue | PartialWithRequiredKeyOf<NewOperateurFormRawValue> {
    return {
      ...operateur,
      dateCreation: operateur.dateCreation ? operateur.dateCreation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
