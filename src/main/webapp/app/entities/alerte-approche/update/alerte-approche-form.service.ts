import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAlerteApproche, NewAlerteApproche } from '../alerte-approche.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlerteApproche for edit and NewAlerteApprocheFormGroupInput for create.
 */
type AlerteApprocheFormGroupInput = IAlerteApproche | PartialWithRequiredKeyOf<NewAlerteApproche>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAlerteApproche | NewAlerteApproche> = Omit<T, 'dateCreation' | 'dateModification' | 'dernierDeclenchement'> & {
  dateCreation?: string | null;
  dateModification?: string | null;
  dernierDeclenchement?: string | null;
};

type AlerteApprocheFormRawValue = FormValueOf<IAlerteApproche>;

type NewAlerteApprocheFormRawValue = FormValueOf<NewAlerteApproche>;

type AlerteApprocheFormDefaults = Pick<NewAlerteApproche, 'id' | 'dateCreation' | 'dateModification' | 'dernierDeclenchement'>;

type AlerteApprocheFormGroupContent = {
  id: FormControl<AlerteApprocheFormRawValue['id'] | NewAlerteApproche['id']>;
  nom: FormControl<AlerteApprocheFormRawValue['nom']>;
  seuilDistance: FormControl<AlerteApprocheFormRawValue['seuilDistance']>;
  seuilTemps: FormControl<AlerteApprocheFormRawValue['seuilTemps']>;
  typeSeuil: FormControl<AlerteApprocheFormRawValue['typeSeuil']>;
  joursActivation: FormControl<AlerteApprocheFormRawValue['joursActivation']>;
  heureDebut: FormControl<AlerteApprocheFormRawValue['heureDebut']>;
  heureFin: FormControl<AlerteApprocheFormRawValue['heureFin']>;
  statut: FormControl<AlerteApprocheFormRawValue['statut']>;
  dateCreation: FormControl<AlerteApprocheFormRawValue['dateCreation']>;
  dateModification: FormControl<AlerteApprocheFormRawValue['dateModification']>;
  dernierDeclenchement: FormControl<AlerteApprocheFormRawValue['dernierDeclenchement']>;
  nombreDeclenchements: FormControl<AlerteApprocheFormRawValue['nombreDeclenchements']>;
  utilisateur: FormControl<AlerteApprocheFormRawValue['utilisateur']>;
};

export type AlerteApprocheFormGroup = FormGroup<AlerteApprocheFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlerteApprocheFormService {
  createAlerteApprocheFormGroup(alerteApproche: AlerteApprocheFormGroupInput = { id: null }): AlerteApprocheFormGroup {
    const alerteApprocheRawValue = this.convertAlerteApprocheToAlerteApprocheRawValue({
      ...this.getFormDefaults(),
      ...alerteApproche,
    });
    return new FormGroup<AlerteApprocheFormGroupContent>({
      id: new FormControl(
        { value: alerteApprocheRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(alerteApprocheRawValue.nom),
      seuilDistance: new FormControl(alerteApprocheRawValue.seuilDistance, {
        validators: [Validators.min(50), Validators.max(1000)],
      }),
      seuilTemps: new FormControl(alerteApprocheRawValue.seuilTemps, {
        validators: [Validators.min(1), Validators.max(60)],
      }),
      typeSeuil: new FormControl(alerteApprocheRawValue.typeSeuil, {
        validators: [Validators.required],
      }),
      joursActivation: new FormControl(alerteApprocheRawValue.joursActivation),
      heureDebut: new FormControl(alerteApprocheRawValue.heureDebut),
      heureFin: new FormControl(alerteApprocheRawValue.heureFin),
      statut: new FormControl(alerteApprocheRawValue.statut, {
        validators: [Validators.required],
      }),
      dateCreation: new FormControl(alerteApprocheRawValue.dateCreation, {
        validators: [Validators.required],
      }),
      dateModification: new FormControl(alerteApprocheRawValue.dateModification),
      dernierDeclenchement: new FormControl(alerteApprocheRawValue.dernierDeclenchement),
      nombreDeclenchements: new FormControl(alerteApprocheRawValue.nombreDeclenchements, {
        validators: [Validators.min(0)],
      }),
      utilisateur: new FormControl(alerteApprocheRawValue.utilisateur),
    });
  }

  getAlerteApproche(form: AlerteApprocheFormGroup): IAlerteApproche | NewAlerteApproche {
    return this.convertAlerteApprocheRawValueToAlerteApproche(
      form.getRawValue() as AlerteApprocheFormRawValue | NewAlerteApprocheFormRawValue,
    );
  }

  resetForm(form: AlerteApprocheFormGroup, alerteApproche: AlerteApprocheFormGroupInput): void {
    const alerteApprocheRawValue = this.convertAlerteApprocheToAlerteApprocheRawValue({ ...this.getFormDefaults(), ...alerteApproche });
    form.reset(
      {
        ...alerteApprocheRawValue,
        id: { value: alerteApprocheRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AlerteApprocheFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreation: currentTime,
      dateModification: currentTime,
      dernierDeclenchement: currentTime,
    };
  }

  private convertAlerteApprocheRawValueToAlerteApproche(
    rawAlerteApproche: AlerteApprocheFormRawValue | NewAlerteApprocheFormRawValue,
  ): IAlerteApproche | NewAlerteApproche {
    return {
      ...rawAlerteApproche,
      dateCreation: dayjs(rawAlerteApproche.dateCreation, DATE_TIME_FORMAT),
      dateModification: dayjs(rawAlerteApproche.dateModification, DATE_TIME_FORMAT),
      dernierDeclenchement: dayjs(rawAlerteApproche.dernierDeclenchement, DATE_TIME_FORMAT),
    };
  }

  private convertAlerteApprocheToAlerteApprocheRawValue(
    alerteApproche: IAlerteApproche | (Partial<NewAlerteApproche> & AlerteApprocheFormDefaults),
  ): AlerteApprocheFormRawValue | PartialWithRequiredKeyOf<NewAlerteApprocheFormRawValue> {
    return {
      ...alerteApproche,
      dateCreation: alerteApproche.dateCreation ? alerteApproche.dateCreation.format(DATE_TIME_FORMAT) : undefined,
      dateModification: alerteApproche.dateModification ? alerteApproche.dateModification.format(DATE_TIME_FORMAT) : undefined,
      dernierDeclenchement: alerteApproche.dernierDeclenchement ? alerteApproche.dernierDeclenchement.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
