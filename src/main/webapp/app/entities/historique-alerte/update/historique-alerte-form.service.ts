import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHistoriqueAlerte, NewHistoriqueAlerte } from '../historique-alerte.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHistoriqueAlerte for edit and NewHistoriqueAlerteFormGroupInput for create.
 */
type HistoriqueAlerteFormGroupInput = IHistoriqueAlerte | PartialWithRequiredKeyOf<NewHistoriqueAlerte>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHistoriqueAlerte | NewHistoriqueAlerte> = Omit<T, 'dateDeclenchement' | 'dateLecture'> & {
  dateDeclenchement?: string | null;
  dateLecture?: string | null;
};

type HistoriqueAlerteFormRawValue = FormValueOf<IHistoriqueAlerte>;

type NewHistoriqueAlerteFormRawValue = FormValueOf<NewHistoriqueAlerte>;

type HistoriqueAlerteFormDefaults = Pick<NewHistoriqueAlerte, 'id' | 'dateDeclenchement' | 'notificationEnvoyee' | 'dateLecture'>;

type HistoriqueAlerteFormGroupContent = {
  id: FormControl<HistoriqueAlerteFormRawValue['id'] | NewHistoriqueAlerte['id']>;
  dateDeclenchement: FormControl<HistoriqueAlerteFormRawValue['dateDeclenchement']>;
  busNumero: FormControl<HistoriqueAlerteFormRawValue['busNumero']>;
  distanceReelle: FormControl<HistoriqueAlerteFormRawValue['distanceReelle']>;
  tempsReel: FormControl<HistoriqueAlerteFormRawValue['tempsReel']>;
  typeDeclenchement: FormControl<HistoriqueAlerteFormRawValue['typeDeclenchement']>;
  notificationEnvoyee: FormControl<HistoriqueAlerteFormRawValue['notificationEnvoyee']>;
  dateLecture: FormControl<HistoriqueAlerteFormRawValue['dateLecture']>;
  bus: FormControl<HistoriqueAlerteFormRawValue['bus']>;
  alerteApproche: FormControl<HistoriqueAlerteFormRawValue['alerteApproche']>;
  utilisateur: FormControl<HistoriqueAlerteFormRawValue['utilisateur']>;
};

export type HistoriqueAlerteFormGroup = FormGroup<HistoriqueAlerteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HistoriqueAlerteFormService {
  createHistoriqueAlerteFormGroup(historiqueAlerte: HistoriqueAlerteFormGroupInput = { id: null }): HistoriqueAlerteFormGroup {
    const historiqueAlerteRawValue = this.convertHistoriqueAlerteToHistoriqueAlerteRawValue({
      ...this.getFormDefaults(),
      ...historiqueAlerte,
    });
    return new FormGroup<HistoriqueAlerteFormGroupContent>({
      id: new FormControl(
        { value: historiqueAlerteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateDeclenchement: new FormControl(historiqueAlerteRawValue.dateDeclenchement, {
        validators: [Validators.required],
      }),
      busNumero: new FormControl(historiqueAlerteRawValue.busNumero),
      distanceReelle: new FormControl(historiqueAlerteRawValue.distanceReelle),
      tempsReel: new FormControl(historiqueAlerteRawValue.tempsReel),
      typeDeclenchement: new FormControl(historiqueAlerteRawValue.typeDeclenchement),
      notificationEnvoyee: new FormControl(historiqueAlerteRawValue.notificationEnvoyee, {
        validators: [Validators.required],
      }),
      dateLecture: new FormControl(historiqueAlerteRawValue.dateLecture),
      bus: new FormControl(historiqueAlerteRawValue.bus),
      alerteApproche: new FormControl(historiqueAlerteRawValue.alerteApproche),
      utilisateur: new FormControl(historiqueAlerteRawValue.utilisateur),
    });
  }

  getHistoriqueAlerte(form: HistoriqueAlerteFormGroup): IHistoriqueAlerte | NewHistoriqueAlerte {
    return this.convertHistoriqueAlerteRawValueToHistoriqueAlerte(
      form.getRawValue() as HistoriqueAlerteFormRawValue | NewHistoriqueAlerteFormRawValue,
    );
  }

  resetForm(form: HistoriqueAlerteFormGroup, historiqueAlerte: HistoriqueAlerteFormGroupInput): void {
    const historiqueAlerteRawValue = this.convertHistoriqueAlerteToHistoriqueAlerteRawValue({
      ...this.getFormDefaults(),
      ...historiqueAlerte,
    });
    form.reset(
      {
        ...historiqueAlerteRawValue,
        id: { value: historiqueAlerteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HistoriqueAlerteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDeclenchement: currentTime,
      notificationEnvoyee: false,
      dateLecture: currentTime,
    };
  }

  private convertHistoriqueAlerteRawValueToHistoriqueAlerte(
    rawHistoriqueAlerte: HistoriqueAlerteFormRawValue | NewHistoriqueAlerteFormRawValue,
  ): IHistoriqueAlerte | NewHistoriqueAlerte {
    return {
      ...rawHistoriqueAlerte,
      dateDeclenchement: dayjs(rawHistoriqueAlerte.dateDeclenchement, DATE_TIME_FORMAT),
      dateLecture: dayjs(rawHistoriqueAlerte.dateLecture, DATE_TIME_FORMAT),
    };
  }

  private convertHistoriqueAlerteToHistoriqueAlerteRawValue(
    historiqueAlerte: IHistoriqueAlerte | (Partial<NewHistoriqueAlerte> & HistoriqueAlerteFormDefaults),
  ): HistoriqueAlerteFormRawValue | PartialWithRequiredKeyOf<NewHistoriqueAlerteFormRawValue> {
    return {
      ...historiqueAlerte,
      dateDeclenchement: historiqueAlerte.dateDeclenchement ? historiqueAlerte.dateDeclenchement.format(DATE_TIME_FORMAT) : undefined,
      dateLecture: historiqueAlerte.dateLecture ? historiqueAlerte.dateLecture.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
