import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITracking, NewTracking } from '../tracking.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITracking for edit and NewTrackingFormGroupInput for create.
 */
type TrackingFormGroupInput = ITracking | PartialWithRequiredKeyOf<NewTracking>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITracking | NewTracking> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type TrackingFormRawValue = FormValueOf<ITracking>;

type NewTrackingFormRawValue = FormValueOf<NewTracking>;

type TrackingFormDefaults = Pick<NewTracking, 'id' | 'timestamp'>;

type TrackingFormGroupContent = {
  id: FormControl<TrackingFormRawValue['id'] | NewTracking['id']>;
  latitude: FormControl<TrackingFormRawValue['latitude']>;
  longitude: FormControl<TrackingFormRawValue['longitude']>;
  vitesse: FormControl<TrackingFormRawValue['vitesse']>;
  cap: FormControl<TrackingFormRawValue['cap']>;
  timestamp: FormControl<TrackingFormRawValue['timestamp']>;
  source: FormControl<TrackingFormRawValue['source']>;
  bus: FormControl<TrackingFormRawValue['bus']>;
};

export type TrackingFormGroup = FormGroup<TrackingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrackingFormService {
  createTrackingFormGroup(tracking: TrackingFormGroupInput = { id: null }): TrackingFormGroup {
    const trackingRawValue = this.convertTrackingToTrackingRawValue({
      ...this.getFormDefaults(),
      ...tracking,
    });
    return new FormGroup<TrackingFormGroupContent>({
      id: new FormControl(
        { value: trackingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      latitude: new FormControl(trackingRawValue.latitude, {
        validators: [Validators.required],
      }),
      longitude: new FormControl(trackingRawValue.longitude, {
        validators: [Validators.required],
      }),
      vitesse: new FormControl(trackingRawValue.vitesse),
      cap: new FormControl(trackingRawValue.cap, {
        validators: [Validators.min(0), Validators.max(359)],
      }),
      timestamp: new FormControl(trackingRawValue.timestamp, {
        validators: [Validators.required],
      }),
      source: new FormControl(trackingRawValue.source),
      bus: new FormControl(trackingRawValue.bus),
    });
  }

  getTracking(form: TrackingFormGroup): ITracking | NewTracking {
    return this.convertTrackingRawValueToTracking(form.getRawValue() as TrackingFormRawValue | NewTrackingFormRawValue);
  }

  resetForm(form: TrackingFormGroup, tracking: TrackingFormGroupInput): void {
    const trackingRawValue = this.convertTrackingToTrackingRawValue({ ...this.getFormDefaults(), ...tracking });
    form.reset(
      {
        ...trackingRawValue,
        id: { value: trackingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrackingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertTrackingRawValueToTracking(rawTracking: TrackingFormRawValue | NewTrackingFormRawValue): ITracking | NewTracking {
    return {
      ...rawTracking,
      timestamp: dayjs(rawTracking.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertTrackingToTrackingRawValue(
    tracking: ITracking | (Partial<NewTracking> & TrackingFormDefaults),
  ): TrackingFormRawValue | PartialWithRequiredKeyOf<NewTrackingFormRawValue> {
    return {
      ...tracking,
      timestamp: tracking.timestamp ? tracking.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
