import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILigne, NewLigne } from '../ligne.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILigne for edit and NewLigneFormGroupInput for create.
 */
type LigneFormGroupInput = ILigne | PartialWithRequiredKeyOf<NewLigne>;

type LigneFormDefaults = Pick<NewLigne, 'id'>;

type LigneFormGroupContent = {
  id: FormControl<ILigne['id'] | NewLigne['id']>;
  numero: FormControl<ILigne['numero']>;
  nom: FormControl<ILigne['nom']>;
  direction: FormControl<ILigne['direction']>;
  couleur: FormControl<ILigne['couleur']>;
  distanceKm: FormControl<ILigne['distanceKm']>;
  dureeMoyenne: FormControl<ILigne['dureeMoyenne']>;
  statut: FormControl<ILigne['statut']>;
  operateur: FormControl<ILigne['operateur']>;
};

export type LigneFormGroup = FormGroup<LigneFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LigneFormService {
  createLigneFormGroup(ligne: LigneFormGroupInput = { id: null }): LigneFormGroup {
    const ligneRawValue = {
      ...this.getFormDefaults(),
      ...ligne,
    };
    return new FormGroup<LigneFormGroupContent>({
      id: new FormControl(
        { value: ligneRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numero: new FormControl(ligneRawValue.numero, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(10)],
      }),
      nom: new FormControl(ligneRawValue.nom, {
        validators: [Validators.required],
      }),
      direction: new FormControl(ligneRawValue.direction, {
        validators: [Validators.required],
      }),
      couleur: new FormControl(ligneRawValue.couleur, {
        validators: [Validators.pattern('^#[0-9A-Fa-f]{6}$')],
      }),
      distanceKm: new FormControl(ligneRawValue.distanceKm),
      dureeMoyenne: new FormControl(ligneRawValue.dureeMoyenne),
      statut: new FormControl(ligneRawValue.statut, {
        validators: [Validators.required],
      }),
      operateur: new FormControl(ligneRawValue.operateur),
    });
  }

  getLigne(form: LigneFormGroup): ILigne | NewLigne {
    return form.getRawValue() as ILigne | NewLigne;
  }

  resetForm(form: LigneFormGroup, ligne: LigneFormGroupInput): void {
    const ligneRawValue = { ...this.getFormDefaults(), ...ligne };
    form.reset(
      {
        ...ligneRawValue,
        id: { value: ligneRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LigneFormDefaults {
    return {
      id: null,
    };
  }
}
