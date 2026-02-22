import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAlerte, NewAlerte } from '../alerte.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlerte for edit and NewAlerteFormGroupInput for create.
 */
type AlerteFormGroupInput = IAlerte | PartialWithRequiredKeyOf<NewAlerte>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAlerte | NewAlerte> = Omit<T, 'heureDebut' | 'heureFin' | 'dernierDeclenchement'> & {
  heureDebut?: string | null;
  heureFin?: string | null;
  dernierDeclenchement?: string | null;
};

type AlerteFormRawValue = FormValueOf<IAlerte>;

type NewAlerteFormRawValue = FormValueOf<NewAlerte>;

type AlerteFormDefaults = Pick<NewAlerte, 'id' | 'heureDebut' | 'heureFin' | 'dernierDeclenchement'>;

type AlerteFormGroupContent = {
  id: FormControl<AlerteFormRawValue['id'] | NewAlerte['id']>;
  typeCible: FormControl<AlerteFormRawValue['typeCible']>;
  cibleId: FormControl<AlerteFormRawValue['cibleId']>;
  seuilMinutes: FormControl<AlerteFormRawValue['seuilMinutes']>;
  joursActivation: FormControl<AlerteFormRawValue['joursActivation']>;
  heureDebut: FormControl<AlerteFormRawValue['heureDebut']>;
  heureFin: FormControl<AlerteFormRawValue['heureFin']>;
  statut: FormControl<AlerteFormRawValue['statut']>;
  dernierDeclenchement: FormControl<AlerteFormRawValue['dernierDeclenchement']>;
  nombreDeclenchements: FormControl<AlerteFormRawValue['nombreDeclenchements']>;
  utilisateur: FormControl<AlerteFormRawValue['utilisateur']>;
};

export type AlerteFormGroup = FormGroup<AlerteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlerteFormService {
  createAlerteFormGroup(alerte: AlerteFormGroupInput = { id: null }): AlerteFormGroup {
    const alerteRawValue = this.convertAlerteToAlerteRawValue({
      ...this.getFormDefaults(),
      ...alerte,
    });
    return new FormGroup<AlerteFormGroupContent>({
      id: new FormControl(
        { value: alerteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      typeCible: new FormControl(alerteRawValue.typeCible, {
        validators: [Validators.required],
      }),
      cibleId: new FormControl(alerteRawValue.cibleId, {
        validators: [Validators.required],
      }),
      seuilMinutes: new FormControl(alerteRawValue.seuilMinutes, {
        validators: [Validators.required, Validators.min(1), Validators.max(60)],
      }),
      joursActivation: new FormControl(alerteRawValue.joursActivation),
      heureDebut: new FormControl(alerteRawValue.heureDebut),
      heureFin: new FormControl(alerteRawValue.heureFin),
      statut: new FormControl(alerteRawValue.statut, {
        validators: [Validators.required],
      }),
      dernierDeclenchement: new FormControl(alerteRawValue.dernierDeclenchement),
      nombreDeclenchements: new FormControl(alerteRawValue.nombreDeclenchements),
      utilisateur: new FormControl(alerteRawValue.utilisateur),
    });
  }

  getAlerte(form: AlerteFormGroup): IAlerte | NewAlerte {
    return this.convertAlerteRawValueToAlerte(form.getRawValue() as AlerteFormRawValue | NewAlerteFormRawValue);
  }

  resetForm(form: AlerteFormGroup, alerte: AlerteFormGroupInput): void {
    const alerteRawValue = this.convertAlerteToAlerteRawValue({ ...this.getFormDefaults(), ...alerte });
    form.reset(
      {
        ...alerteRawValue,
        id: { value: alerteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AlerteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      heureDebut: currentTime,
      heureFin: currentTime,
      dernierDeclenchement: currentTime,
    };
  }

  private convertAlerteRawValueToAlerte(rawAlerte: AlerteFormRawValue | NewAlerteFormRawValue): IAlerte | NewAlerte {
    return {
      ...rawAlerte,
      heureDebut: dayjs(rawAlerte.heureDebut, DATE_TIME_FORMAT),
      heureFin: dayjs(rawAlerte.heureFin, DATE_TIME_FORMAT),
      dernierDeclenchement: dayjs(rawAlerte.dernierDeclenchement, DATE_TIME_FORMAT),
    };
  }

  private convertAlerteToAlerteRawValue(
    alerte: IAlerte | (Partial<NewAlerte> & AlerteFormDefaults),
  ): AlerteFormRawValue | PartialWithRequiredKeyOf<NewAlerteFormRawValue> {
    return {
      ...alerte,
      heureDebut: alerte.heureDebut ? alerte.heureDebut.format(DATE_TIME_FORMAT) : undefined,
      heureFin: alerte.heureFin ? alerte.heureFin.format(DATE_TIME_FORMAT) : undefined,
      dernierDeclenchement: alerte.dernierDeclenchement ? alerte.dernierDeclenchement.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
