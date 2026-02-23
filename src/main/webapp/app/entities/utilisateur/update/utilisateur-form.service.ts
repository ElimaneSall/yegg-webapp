import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUtilisateur, NewUtilisateur } from '../utilisateur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtilisateur for edit and NewUtilisateurFormGroupInput for create.
 */
type UtilisateurFormGroupInput = IUtilisateur | PartialWithRequiredKeyOf<NewUtilisateur>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUtilisateur | NewUtilisateur> = Omit<T, 'dateCreation' | 'derniereConnexion'> & {
  dateCreation?: string | null;
  derniereConnexion?: string | null;
};

type UtilisateurFormRawValue = FormValueOf<IUtilisateur>;

type NewUtilisateurFormRawValue = FormValueOf<NewUtilisateur>;

type UtilisateurFormDefaults = Pick<
  NewUtilisateur,
  'id' | 'notificationsPush' | 'notificationsSms' | 'dateCreation' | 'derniereConnexion' | 'actif'
>;

type UtilisateurFormGroupContent = {
  id: FormControl<UtilisateurFormRawValue['id'] | NewUtilisateur['id']>;
  prenom: FormControl<UtilisateurFormRawValue['prenom']>;
  nom: FormControl<UtilisateurFormRawValue['nom']>;
  email: FormControl<UtilisateurFormRawValue['email']>;
  telephone: FormControl<UtilisateurFormRawValue['telephone']>;
  motDePasse: FormControl<UtilisateurFormRawValue['motDePasse']>;
  role: FormControl<UtilisateurFormRawValue['role']>;
  matricule: FormControl<UtilisateurFormRawValue['matricule']>;
  fcmToken: FormControl<UtilisateurFormRawValue['fcmToken']>;
  notificationsPush: FormControl<UtilisateurFormRawValue['notificationsPush']>;
  notificationsSms: FormControl<UtilisateurFormRawValue['notificationsSms']>;
  langue: FormControl<UtilisateurFormRawValue['langue']>;
  photo: FormControl<UtilisateurFormRawValue['photo']>;
  photoContentType: FormControl<UtilisateurFormRawValue['photoContentType']>;
  dateCreation: FormControl<UtilisateurFormRawValue['dateCreation']>;
  derniereConnexion: FormControl<UtilisateurFormRawValue['derniereConnexion']>;
  actif: FormControl<UtilisateurFormRawValue['actif']>;
  dateEmbauche: FormControl<UtilisateurFormRawValue['dateEmbauche']>;
  numeroPermis: FormControl<UtilisateurFormRawValue['numeroPermis']>;
};

export type UtilisateurFormGroup = FormGroup<UtilisateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtilisateurFormService {
  createUtilisateurFormGroup(utilisateur: UtilisateurFormGroupInput = { id: null }): UtilisateurFormGroup {
    const utilisateurRawValue = this.convertUtilisateurToUtilisateurRawValue({
      ...this.getFormDefaults(),
      ...utilisateur,
    });
    return new FormGroup<UtilisateurFormGroupContent>({
      id: new FormControl(
        { value: utilisateurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      prenom: new FormControl(utilisateurRawValue.prenom),
      nom: new FormControl(utilisateurRawValue.nom),
      email: new FormControl(utilisateurRawValue.email),
      telephone: new FormControl(utilisateurRawValue.telephone),
      motDePasse: new FormControl(utilisateurRawValue.motDePasse),
      role: new FormControl(utilisateurRawValue.role, {
        validators: [Validators.required],
      }),
      matricule: new FormControl(utilisateurRawValue.matricule),
      fcmToken: new FormControl(utilisateurRawValue.fcmToken),
      notificationsPush: new FormControl(utilisateurRawValue.notificationsPush),
      notificationsSms: new FormControl(utilisateurRawValue.notificationsSms),
      langue: new FormControl(utilisateurRawValue.langue),
      photo: new FormControl(utilisateurRawValue.photo),
      photoContentType: new FormControl(utilisateurRawValue.photoContentType),
      dateCreation: new FormControl(utilisateurRawValue.dateCreation, {
        validators: [Validators.required],
      }),
      derniereConnexion: new FormControl(utilisateurRawValue.derniereConnexion),
      actif: new FormControl(utilisateurRawValue.actif, {
        validators: [Validators.required],
      }),
      dateEmbauche: new FormControl(utilisateurRawValue.dateEmbauche),
      numeroPermis: new FormControl(utilisateurRawValue.numeroPermis),
    });
  }

  getUtilisateur(form: UtilisateurFormGroup): IUtilisateur | NewUtilisateur {
    return this.convertUtilisateurRawValueToUtilisateur(form.getRawValue() as UtilisateurFormRawValue | NewUtilisateurFormRawValue);
  }

  resetForm(form: UtilisateurFormGroup, utilisateur: UtilisateurFormGroupInput): void {
    const utilisateurRawValue = this.convertUtilisateurToUtilisateurRawValue({ ...this.getFormDefaults(), ...utilisateur });
    form.reset(
      {
        ...utilisateurRawValue,
        id: { value: utilisateurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UtilisateurFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      notificationsPush: false,
      notificationsSms: false,
      dateCreation: currentTime,
      derniereConnexion: currentTime,
      actif: false,
    };
  }

  private convertUtilisateurRawValueToUtilisateur(
    rawUtilisateur: UtilisateurFormRawValue | NewUtilisateurFormRawValue,
  ): IUtilisateur | NewUtilisateur {
    return {
      ...rawUtilisateur,
      dateCreation: dayjs(rawUtilisateur.dateCreation, DATE_TIME_FORMAT),
      derniereConnexion: dayjs(rawUtilisateur.derniereConnexion, DATE_TIME_FORMAT),
    };
  }

  private convertUtilisateurToUtilisateurRawValue(
    utilisateur: IUtilisateur | (Partial<NewUtilisateur> & UtilisateurFormDefaults),
  ): UtilisateurFormRawValue | PartialWithRequiredKeyOf<NewUtilisateurFormRawValue> {
    return {
      ...utilisateur,
      dateCreation: utilisateur.dateCreation ? utilisateur.dateCreation.format(DATE_TIME_FORMAT) : undefined,
      derniereConnexion: utilisateur.derniereConnexion ? utilisateur.derniereConnexion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
