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

type LigneFormDefaults = Pick<NewLigne, 'id' | 'actif'>;

type LigneFormGroupContent = {
  id: FormControl<ILigne['id'] | NewLigne['id']>;
  numero: FormControl<ILigne['numero']>;
  nom: FormControl<ILigne['nom']>;
  direction: FormControl<ILigne['direction']>;
  description: FormControl<ILigne['description']>;
  couleur: FormControl<ILigne['couleur']>;
  distanceKm: FormControl<ILigne['distanceKm']>;
  dureeMoyenne: FormControl<ILigne['dureeMoyenne']>;
  frequence: FormControl<ILigne['frequence']>;
  statut: FormControl<ILigne['statut']>;
  joursFeries: FormControl<ILigne['joursFeries']>;
  dateDebut: FormControl<ILigne['dateDebut']>;
  dateFin: FormControl<ILigne['dateFin']>;
  actif: FormControl<ILigne['actif']>;
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
      description: new FormControl(ligneRawValue.description),
      couleur: new FormControl(ligneRawValue.couleur),
      distanceKm: new FormControl(ligneRawValue.distanceKm, {
        validators: [Validators.min(0)],
      }),
      dureeMoyenne: new FormControl(ligneRawValue.dureeMoyenne, {
        validators: [Validators.min(0)],
      }),
      frequence: new FormControl(ligneRawValue.frequence, {
        validators: [Validators.min(1), Validators.max(60)],
      }),
      statut: new FormControl(ligneRawValue.statut, {
        validators: [Validators.required],
      }),
      joursFeries: new FormControl(ligneRawValue.joursFeries),
      dateDebut: new FormControl(ligneRawValue.dateDebut),
      dateFin: new FormControl(ligneRawValue.dateFin),
      actif: new FormControl(ligneRawValue.actif, {
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
      actif: false,
    };
  }
}
