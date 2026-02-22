import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBus, NewBus } from '../bus.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBus for edit and NewBusFormGroupInput for create.
 */
type BusFormGroupInput = IBus | PartialWithRequiredKeyOf<NewBus>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBus | NewBus> = Omit<T, 'gpsLastPing' | 'positionUpdatedAt'> & {
  gpsLastPing?: string | null;
  positionUpdatedAt?: string | null;
};

type BusFormRawValue = FormValueOf<IBus>;

type NewBusFormRawValue = FormValueOf<NewBus>;

type BusFormDefaults = Pick<NewBus, 'id' | 'gpsLastPing' | 'positionUpdatedAt'>;

type BusFormGroupContent = {
  id: FormControl<BusFormRawValue['id'] | NewBus['id']>;
  numeroVehicule: FormControl<BusFormRawValue['numeroVehicule']>;
  plaque: FormControl<BusFormRawValue['plaque']>;
  modele: FormControl<BusFormRawValue['modele']>;
  capacite: FormControl<BusFormRawValue['capacite']>;
  anneeFabrication: FormControl<BusFormRawValue['anneeFabrication']>;
  gpsDeviceId: FormControl<BusFormRawValue['gpsDeviceId']>;
  gpsStatus: FormControl<BusFormRawValue['gpsStatus']>;
  gpsLastPing: FormControl<BusFormRawValue['gpsLastPing']>;
  gpsBatteryLevel: FormControl<BusFormRawValue['gpsBatteryLevel']>;
  currentLatitude: FormControl<BusFormRawValue['currentLatitude']>;
  currentLongitude: FormControl<BusFormRawValue['currentLongitude']>;
  currentVitesse: FormControl<BusFormRawValue['currentVitesse']>;
  currentCap: FormControl<BusFormRawValue['currentCap']>;
  positionUpdatedAt: FormControl<BusFormRawValue['positionUpdatedAt']>;
  statut: FormControl<BusFormRawValue['statut']>;
  utilisateur: FormControl<BusFormRawValue['utilisateur']>;
  ligne: FormControl<BusFormRawValue['ligne']>;
};

export type BusFormGroup = FormGroup<BusFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BusFormService {
  createBusFormGroup(bus: BusFormGroupInput = { id: null }): BusFormGroup {
    const busRawValue = this.convertBusToBusRawValue({
      ...this.getFormDefaults(),
      ...bus,
    });
    return new FormGroup<BusFormGroupContent>({
      id: new FormControl(
        { value: busRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numeroVehicule: new FormControl(busRawValue.numeroVehicule, {
        validators: [Validators.required],
      }),
      plaque: new FormControl(busRawValue.plaque, {
        validators: [Validators.required],
      }),
      modele: new FormControl(busRawValue.modele),
      capacite: new FormControl(busRawValue.capacite, {
        validators: [Validators.required, Validators.min(1), Validators.max(200)],
      }),
      anneeFabrication: new FormControl(busRawValue.anneeFabrication),
      gpsDeviceId: new FormControl(busRawValue.gpsDeviceId),
      gpsStatus: new FormControl(busRawValue.gpsStatus),
      gpsLastPing: new FormControl(busRawValue.gpsLastPing),
      gpsBatteryLevel: new FormControl(busRawValue.gpsBatteryLevel, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      currentLatitude: new FormControl(busRawValue.currentLatitude),
      currentLongitude: new FormControl(busRawValue.currentLongitude),
      currentVitesse: new FormControl(busRawValue.currentVitesse),
      currentCap: new FormControl(busRawValue.currentCap, {
        validators: [Validators.min(0), Validators.max(359)],
      }),
      positionUpdatedAt: new FormControl(busRawValue.positionUpdatedAt),
      statut: new FormControl(busRawValue.statut, {
        validators: [Validators.required],
      }),
      utilisateur: new FormControl(busRawValue.utilisateur),
      ligne: new FormControl(busRawValue.ligne),
    });
  }

  getBus(form: BusFormGroup): IBus | NewBus {
    return this.convertBusRawValueToBus(form.getRawValue() as BusFormRawValue | NewBusFormRawValue);
  }

  resetForm(form: BusFormGroup, bus: BusFormGroupInput): void {
    const busRawValue = this.convertBusToBusRawValue({ ...this.getFormDefaults(), ...bus });
    form.reset(
      {
        ...busRawValue,
        id: { value: busRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BusFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      gpsLastPing: currentTime,
      positionUpdatedAt: currentTime,
    };
  }

  private convertBusRawValueToBus(rawBus: BusFormRawValue | NewBusFormRawValue): IBus | NewBus {
    return {
      ...rawBus,
      gpsLastPing: dayjs(rawBus.gpsLastPing, DATE_TIME_FORMAT),
      positionUpdatedAt: dayjs(rawBus.positionUpdatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertBusToBusRawValue(
    bus: IBus | (Partial<NewBus> & BusFormDefaults),
  ): BusFormRawValue | PartialWithRequiredKeyOf<NewBusFormRawValue> {
    return {
      ...bus,
      gpsLastPing: bus.gpsLastPing ? bus.gpsLastPing.format(DATE_TIME_FORMAT) : undefined,
      positionUpdatedAt: bus.positionUpdatedAt ? bus.positionUpdatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
