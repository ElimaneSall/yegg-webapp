import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAlerteLigneArret, NewAlerteLigneArret } from '../alerte-ligne-arret.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlerteLigneArret for edit and NewAlerteLigneArretFormGroupInput for create.
 */
type AlerteLigneArretFormGroupInput = IAlerteLigneArret | PartialWithRequiredKeyOf<NewAlerteLigneArret>;

type AlerteLigneArretFormDefaults = Pick<NewAlerteLigneArret, 'id' | 'actif'>;

type AlerteLigneArretFormGroupContent = {
  id: FormControl<IAlerteLigneArret['id'] | NewAlerteLigneArret['id']>;
  sens: FormControl<IAlerteLigneArret['sens']>;
  actif: FormControl<IAlerteLigneArret['actif']>;
  ligne: FormControl<IAlerteLigneArret['ligne']>;
  arret: FormControl<IAlerteLigneArret['arret']>;
  alerteApproche: FormControl<IAlerteLigneArret['alerteApproche']>;
};

export type AlerteLigneArretFormGroup = FormGroup<AlerteLigneArretFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlerteLigneArretFormService {
  createAlerteLigneArretFormGroup(alerteLigneArret: AlerteLigneArretFormGroupInput = { id: null }): AlerteLigneArretFormGroup {
    const alerteLigneArretRawValue = {
      ...this.getFormDefaults(),
      ...alerteLigneArret,
    };
    return new FormGroup<AlerteLigneArretFormGroupContent>({
      id: new FormControl(
        { value: alerteLigneArretRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sens: new FormControl(alerteLigneArretRawValue.sens),
      actif: new FormControl(alerteLigneArretRawValue.actif),
      ligne: new FormControl(alerteLigneArretRawValue.ligne),
      arret: new FormControl(alerteLigneArretRawValue.arret),
      alerteApproche: new FormControl(alerteLigneArretRawValue.alerteApproche),
    });
  }

  getAlerteLigneArret(form: AlerteLigneArretFormGroup): IAlerteLigneArret | NewAlerteLigneArret {
    return form.getRawValue() as IAlerteLigneArret | NewAlerteLigneArret;
  }

  resetForm(form: AlerteLigneArretFormGroup, alerteLigneArret: AlerteLigneArretFormGroupInput): void {
    const alerteLigneArretRawValue = { ...this.getFormDefaults(), ...alerteLigneArret };
    form.reset(
      {
        ...alerteLigneArretRawValue,
        id: { value: alerteLigneArretRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AlerteLigneArretFormDefaults {
    return {
      id: null,
      actif: false,
    };
  }
}
