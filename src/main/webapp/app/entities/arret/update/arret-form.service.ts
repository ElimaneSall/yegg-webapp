import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IArret, NewArret } from '../arret.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArret for edit and NewArretFormGroupInput for create.
 */
type ArretFormGroupInput = IArret | PartialWithRequiredKeyOf<NewArret>;

type ArretFormDefaults = Pick<NewArret, 'id' | 'actif'>;

type ArretFormGroupContent = {
  id: FormControl<IArret['id'] | NewArret['id']>;
  nom: FormControl<IArret['nom']>;
  code: FormControl<IArret['code']>;
  latitude: FormControl<IArret['latitude']>;
  longitude: FormControl<IArret['longitude']>;
  adresse: FormControl<IArret['adresse']>;
  zoneTarifaire: FormControl<IArret['zoneTarifaire']>;
  equipements: FormControl<IArret['equipements']>;
  actif: FormControl<IArret['actif']>;
};

export type ArretFormGroup = FormGroup<ArretFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArretFormService {
  createArretFormGroup(arret: ArretFormGroupInput = { id: null }): ArretFormGroup {
    const arretRawValue = {
      ...this.getFormDefaults(),
      ...arret,
    };
    return new FormGroup<ArretFormGroupContent>({
      id: new FormControl(
        { value: arretRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(arretRawValue.nom, {
        validators: [Validators.required],
      }),
      code: new FormControl(arretRawValue.code, {
        validators: [Validators.required],
      }),
      latitude: new FormControl(arretRawValue.latitude, {
        validators: [Validators.required, Validators.min(-90), Validators.max(90)],
      }),
      longitude: new FormControl(arretRawValue.longitude, {
        validators: [Validators.required, Validators.min(-180), Validators.max(180)],
      }),
      adresse: new FormControl(arretRawValue.adresse),
      zoneTarifaire: new FormControl(arretRawValue.zoneTarifaire),
      equipements: new FormControl(arretRawValue.equipements),
      actif: new FormControl(arretRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getArret(form: ArretFormGroup): IArret | NewArret {
    return form.getRawValue() as IArret | NewArret;
  }

  resetForm(form: ArretFormGroup, arret: ArretFormGroupInput): void {
    const arretRawValue = { ...this.getFormDefaults(), ...arret };
    form.reset(
      {
        ...arretRawValue,
        id: { value: arretRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ArretFormDefaults {
    return {
      id: null,
      actif: false,
    };
  }
}
