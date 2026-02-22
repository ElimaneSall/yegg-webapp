import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotification, NewNotification } from '../notification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotification for edit and NewNotificationFormGroupInput for create.
 */
type NotificationFormGroupInput = INotification | PartialWithRequiredKeyOf<NewNotification>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotification | NewNotification> = Omit<T, 'dateLecture'> & {
  dateLecture?: string | null;
};

type NotificationFormRawValue = FormValueOf<INotification>;

type NewNotificationFormRawValue = FormValueOf<NewNotification>;

type NotificationFormDefaults = Pick<NewNotification, 'id' | 'lu' | 'dateLecture'>;

type NotificationFormGroupContent = {
  id: FormControl<NotificationFormRawValue['id'] | NewNotification['id']>;
  type: FormControl<NotificationFormRawValue['type']>;
  titre: FormControl<NotificationFormRawValue['titre']>;
  message: FormControl<NotificationFormRawValue['message']>;
  donnees: FormControl<NotificationFormRawValue['donnees']>;
  priorite: FormControl<NotificationFormRawValue['priorite']>;
  statut: FormControl<NotificationFormRawValue['statut']>;
  lu: FormControl<NotificationFormRawValue['lu']>;
  dateLecture: FormControl<NotificationFormRawValue['dateLecture']>;
  utilisateur: FormControl<NotificationFormRawValue['utilisateur']>;
};

export type NotificationFormGroup = FormGroup<NotificationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificationFormService {
  createNotificationFormGroup(notification: NotificationFormGroupInput = { id: null }): NotificationFormGroup {
    const notificationRawValue = this.convertNotificationToNotificationRawValue({
      ...this.getFormDefaults(),
      ...notification,
    });
    return new FormGroup<NotificationFormGroupContent>({
      id: new FormControl(
        { value: notificationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(notificationRawValue.type),
      titre: new FormControl(notificationRawValue.titre, {
        validators: [Validators.required],
      }),
      message: new FormControl(notificationRawValue.message, {
        validators: [Validators.required],
      }),
      donnees: new FormControl(notificationRawValue.donnees),
      priorite: new FormControl(notificationRawValue.priorite),
      statut: new FormControl(notificationRawValue.statut),
      lu: new FormControl(notificationRawValue.lu),
      dateLecture: new FormControl(notificationRawValue.dateLecture),
      utilisateur: new FormControl(notificationRawValue.utilisateur),
    });
  }

  getNotification(form: NotificationFormGroup): INotification | NewNotification {
    return this.convertNotificationRawValueToNotification(form.getRawValue() as NotificationFormRawValue | NewNotificationFormRawValue);
  }

  resetForm(form: NotificationFormGroup, notification: NotificationFormGroupInput): void {
    const notificationRawValue = this.convertNotificationToNotificationRawValue({ ...this.getFormDefaults(), ...notification });
    form.reset(
      {
        ...notificationRawValue,
        id: { value: notificationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NotificationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lu: false,
      dateLecture: currentTime,
    };
  }

  private convertNotificationRawValueToNotification(
    rawNotification: NotificationFormRawValue | NewNotificationFormRawValue,
  ): INotification | NewNotification {
    return {
      ...rawNotification,
      dateLecture: dayjs(rawNotification.dateLecture, DATE_TIME_FORMAT),
    };
  }

  private convertNotificationToNotificationRawValue(
    notification: INotification | (Partial<NewNotification> & NotificationFormDefaults),
  ): NotificationFormRawValue | PartialWithRequiredKeyOf<NewNotificationFormRawValue> {
    return {
      ...notification,
      dateLecture: notification.dateLecture ? notification.dateLecture.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
