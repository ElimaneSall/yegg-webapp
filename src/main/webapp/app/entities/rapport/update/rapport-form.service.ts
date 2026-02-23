import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRapport, NewRapport } from '../rapport.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRapport for edit and NewRapportFormGroupInput for create.
 */
type RapportFormGroupInput = IRapport | PartialWithRequiredKeyOf<NewRapport>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRapport | NewRapport> = Omit<T, 'dateGeneration'> & {
  dateGeneration?: string | null;
};

type RapportFormRawValue = FormValueOf<IRapport>;

type NewRapportFormRawValue = FormValueOf<NewRapport>;

type RapportFormDefaults = Pick<NewRapport, 'id' | 'dateGeneration'>;

type RapportFormGroupContent = {
  id: FormControl<RapportFormRawValue['id'] | NewRapport['id']>;
  nom: FormControl<RapportFormRawValue['nom']>;
  type: FormControl<RapportFormRawValue['type']>;
  periodeDebut: FormControl<RapportFormRawValue['periodeDebut']>;
  periodeFin: FormControl<RapportFormRawValue['periodeFin']>;
  format: FormControl<RapportFormRawValue['format']>;
  contenu: FormControl<RapportFormRawValue['contenu']>;
  dateGeneration: FormControl<RapportFormRawValue['dateGeneration']>;
  generePar: FormControl<RapportFormRawValue['generePar']>;
  operateur: FormControl<RapportFormRawValue['operateur']>;
  admin: FormControl<RapportFormRawValue['admin']>;
};

export type RapportFormGroup = FormGroup<RapportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RapportFormService {
  createRapportFormGroup(rapport: RapportFormGroupInput = { id: null }): RapportFormGroup {
    const rapportRawValue = this.convertRapportToRapportRawValue({
      ...this.getFormDefaults(),
      ...rapport,
    });
    return new FormGroup<RapportFormGroupContent>({
      id: new FormControl(
        { value: rapportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(rapportRawValue.nom, {
        validators: [Validators.required],
      }),
      type: new FormControl(rapportRawValue.type, {
        validators: [Validators.required],
      }),
      periodeDebut: new FormControl(rapportRawValue.periodeDebut, {
        validators: [Validators.required],
      }),
      periodeFin: new FormControl(rapportRawValue.periodeFin, {
        validators: [Validators.required],
      }),
      format: new FormControl(rapportRawValue.format),
      contenu: new FormControl(rapportRawValue.contenu),
      dateGeneration: new FormControl(rapportRawValue.dateGeneration, {
        validators: [Validators.required],
      }),
      generePar: new FormControl(rapportRawValue.generePar),
      operateur: new FormControl(rapportRawValue.operateur),
      admin: new FormControl(rapportRawValue.admin),
    });
  }

  getRapport(form: RapportFormGroup): IRapport | NewRapport {
    return this.convertRapportRawValueToRapport(form.getRawValue() as RapportFormRawValue | NewRapportFormRawValue);
  }

  resetForm(form: RapportFormGroup, rapport: RapportFormGroupInput): void {
    const rapportRawValue = this.convertRapportToRapportRawValue({ ...this.getFormDefaults(), ...rapport });
    form.reset(
      {
        ...rapportRawValue,
        id: { value: rapportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RapportFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateGeneration: currentTime,
    };
  }

  private convertRapportRawValueToRapport(rawRapport: RapportFormRawValue | NewRapportFormRawValue): IRapport | NewRapport {
    return {
      ...rawRapport,
      dateGeneration: dayjs(rawRapport.dateGeneration, DATE_TIME_FORMAT),
    };
  }

  private convertRapportToRapportRawValue(
    rapport: IRapport | (Partial<NewRapport> & RapportFormDefaults),
  ): RapportFormRawValue | PartialWithRequiredKeyOf<NewRapportFormRawValue> {
    return {
      ...rapport,
      dateGeneration: rapport.dateGeneration ? rapport.dateGeneration.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
