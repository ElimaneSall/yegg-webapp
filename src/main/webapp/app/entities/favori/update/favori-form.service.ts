import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFavori, NewFavori } from '../favori.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFavori for edit and NewFavoriFormGroupInput for create.
 */
type FavoriFormGroupInput = IFavori | PartialWithRequiredKeyOf<NewFavori>;

type FavoriFormDefaults = Pick<NewFavori, 'id' | 'alerteActive'>;

type FavoriFormGroupContent = {
  id: FormControl<IFavori['id'] | NewFavori['id']>;
  type: FormControl<IFavori['type']>;
  cibleId: FormControl<IFavori['cibleId']>;
  nomPersonnalise: FormControl<IFavori['nomPersonnalise']>;
  ordre: FormControl<IFavori['ordre']>;
  alerteActive: FormControl<IFavori['alerteActive']>;
  alerteSeuil: FormControl<IFavori['alerteSeuil']>;
  utilisateur: FormControl<IFavori['utilisateur']>;
};

export type FavoriFormGroup = FormGroup<FavoriFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FavoriFormService {
  createFavoriFormGroup(favori: FavoriFormGroupInput = { id: null }): FavoriFormGroup {
    const favoriRawValue = {
      ...this.getFormDefaults(),
      ...favori,
    };
    return new FormGroup<FavoriFormGroupContent>({
      id: new FormControl(
        { value: favoriRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(favoriRawValue.type, {
        validators: [Validators.required],
      }),
      cibleId: new FormControl(favoriRawValue.cibleId, {
        validators: [Validators.required],
      }),
      nomPersonnalise: new FormControl(favoriRawValue.nomPersonnalise),
      ordre: new FormControl(favoriRawValue.ordre),
      alerteActive: new FormControl(favoriRawValue.alerteActive),
      alerteSeuil: new FormControl(favoriRawValue.alerteSeuil, {
        validators: [Validators.min(1), Validators.max(60)],
      }),
      utilisateur: new FormControl(favoriRawValue.utilisateur),
    });
  }

  getFavori(form: FavoriFormGroup): IFavori | NewFavori {
    return form.getRawValue() as IFavori | NewFavori;
  }

  resetForm(form: FavoriFormGroup, favori: FavoriFormGroupInput): void {
    const favoriRawValue = { ...this.getFormDefaults(), ...favori };
    form.reset(
      {
        ...favoriRawValue,
        id: { value: favoriRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FavoriFormDefaults {
    return {
      id: null,
      alerteActive: false,
    };
  }
}
