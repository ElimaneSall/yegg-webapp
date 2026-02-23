import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFavori | NewFavori> = Omit<T, 'dateAjout' | 'dernierAcces'> & {
  dateAjout?: string | null;
  dernierAcces?: string | null;
};

type FavoriFormRawValue = FormValueOf<IFavori>;

type NewFavoriFormRawValue = FormValueOf<NewFavori>;

type FavoriFormDefaults = Pick<NewFavori, 'id' | 'dateAjout' | 'dernierAcces'>;

type FavoriFormGroupContent = {
  id: FormControl<FavoriFormRawValue['id'] | NewFavori['id']>;
  type: FormControl<FavoriFormRawValue['type']>;
  cibleId: FormControl<FavoriFormRawValue['cibleId']>;
  nomPersonnalise: FormControl<FavoriFormRawValue['nomPersonnalise']>;
  ordre: FormControl<FavoriFormRawValue['ordre']>;
  dateAjout: FormControl<FavoriFormRawValue['dateAjout']>;
  dernierAcces: FormControl<FavoriFormRawValue['dernierAcces']>;
  utilisateur: FormControl<FavoriFormRawValue['utilisateur']>;
};

export type FavoriFormGroup = FormGroup<FavoriFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FavoriFormService {
  createFavoriFormGroup(favori: FavoriFormGroupInput = { id: null }): FavoriFormGroup {
    const favoriRawValue = this.convertFavoriToFavoriRawValue({
      ...this.getFormDefaults(),
      ...favori,
    });
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
      dateAjout: new FormControl(favoriRawValue.dateAjout, {
        validators: [Validators.required],
      }),
      dernierAcces: new FormControl(favoriRawValue.dernierAcces),
      utilisateur: new FormControl(favoriRawValue.utilisateur),
    });
  }

  getFavori(form: FavoriFormGroup): IFavori | NewFavori {
    return this.convertFavoriRawValueToFavori(form.getRawValue() as FavoriFormRawValue | NewFavoriFormRawValue);
  }

  resetForm(form: FavoriFormGroup, favori: FavoriFormGroupInput): void {
    const favoriRawValue = this.convertFavoriToFavoriRawValue({ ...this.getFormDefaults(), ...favori });
    form.reset(
      {
        ...favoriRawValue,
        id: { value: favoriRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FavoriFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateAjout: currentTime,
      dernierAcces: currentTime,
    };
  }

  private convertFavoriRawValueToFavori(rawFavori: FavoriFormRawValue | NewFavoriFormRawValue): IFavori | NewFavori {
    return {
      ...rawFavori,
      dateAjout: dayjs(rawFavori.dateAjout, DATE_TIME_FORMAT),
      dernierAcces: dayjs(rawFavori.dernierAcces, DATE_TIME_FORMAT),
    };
  }

  private convertFavoriToFavoriRawValue(
    favori: IFavori | (Partial<NewFavori> & FavoriFormDefaults),
  ): FavoriFormRawValue | PartialWithRequiredKeyOf<NewFavoriFormRawValue> {
    return {
      ...favori,
      dateAjout: favori.dateAjout ? favori.dateAjout.format(DATE_TIME_FORMAT) : undefined,
      dernierAcces: favori.dernierAcces ? favori.dernierAcces.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
