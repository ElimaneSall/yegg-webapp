import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILigneArret, NewLigneArret } from '../ligne-arret.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILigneArret for edit and NewLigneArretFormGroupInput for create.
 */
type LigneArretFormGroupInput = ILigneArret | PartialWithRequiredKeyOf<NewLigneArret>;

type LigneArretFormDefaults = Pick<NewLigneArret, 'id'>;

type LigneArretFormGroupContent = {
  id: FormControl<ILigneArret['id'] | NewLigneArret['id']>;
  ordre: FormControl<ILigneArret['ordre']>;
  tempsTrajetDepart: FormControl<ILigneArret['tempsTrajetDepart']>;
  distanceDepart: FormControl<ILigneArret['distanceDepart']>;
  ligne: FormControl<ILigneArret['ligne']>;
  arret: FormControl<ILigneArret['arret']>;
};

export type LigneArretFormGroup = FormGroup<LigneArretFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LigneArretFormService {
  createLigneArretFormGroup(ligneArret: LigneArretFormGroupInput = { id: null }): LigneArretFormGroup {
    const ligneArretRawValue = {
      ...this.getFormDefaults(),
      ...ligneArret,
    };
    return new FormGroup<LigneArretFormGroupContent>({
      id: new FormControl(
        { value: ligneArretRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ordre: new FormControl(ligneArretRawValue.ordre, {
        validators: [Validators.required],
      }),
      tempsTrajetDepart: new FormControl(ligneArretRawValue.tempsTrajetDepart),
      distanceDepart: new FormControl(ligneArretRawValue.distanceDepart),
      ligne: new FormControl(ligneArretRawValue.ligne),
      arret: new FormControl(ligneArretRawValue.arret),
    });
  }

  getLigneArret(form: LigneArretFormGroup): ILigneArret | NewLigneArret {
    return form.getRawValue() as ILigneArret | NewLigneArret;
  }

  resetForm(form: LigneArretFormGroup, ligneArret: LigneArretFormGroupInput): void {
    const ligneArretRawValue = { ...this.getFormDefaults(), ...ligneArret };
    form.reset(
      {
        ...ligneArretRawValue,
        id: { value: ligneArretRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LigneArretFormDefaults {
    return {
      id: null,
    };
  }
}
