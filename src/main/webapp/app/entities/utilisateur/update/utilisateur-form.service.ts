import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

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

type UtilisateurFormDefaults = Pick<NewUtilisateur, 'id' | 'notificationsPush'>;

type UtilisateurFormGroupContent = {
  id: FormControl<IUtilisateur['id'] | NewUtilisateur['id']>;
  matricule: FormControl<IUtilisateur['matricule']>;
  telephone: FormControl<IUtilisateur['telephone']>;
  fcmToken: FormControl<IUtilisateur['fcmToken']>;
  notificationsPush: FormControl<IUtilisateur['notificationsPush']>;
  langue: FormControl<IUtilisateur['langue']>;
  dateEmbauche: FormControl<IUtilisateur['dateEmbauche']>;
  numeroPermis: FormControl<IUtilisateur['numeroPermis']>;
};

export type UtilisateurFormGroup = FormGroup<UtilisateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtilisateurFormService {
  createUtilisateurFormGroup(utilisateur: UtilisateurFormGroupInput = { id: null }): UtilisateurFormGroup {
    const utilisateurRawValue = {
      ...this.getFormDefaults(),
      ...utilisateur,
    };
    return new FormGroup<UtilisateurFormGroupContent>({
      id: new FormControl(
        { value: utilisateurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      matricule: new FormControl(utilisateurRawValue.matricule),
      telephone: new FormControl(utilisateurRawValue.telephone, {
        validators: [Validators.pattern('^[0-9]{9,15}$')],
      }),
      fcmToken: new FormControl(utilisateurRawValue.fcmToken),
      notificationsPush: new FormControl(utilisateurRawValue.notificationsPush),
      langue: new FormControl(utilisateurRawValue.langue),
      dateEmbauche: new FormControl(utilisateurRawValue.dateEmbauche),
      numeroPermis: new FormControl(utilisateurRawValue.numeroPermis),
    });
  }

  getUtilisateur(form: UtilisateurFormGroup): IUtilisateur | NewUtilisateur {
    return form.getRawValue() as IUtilisateur | NewUtilisateur;
  }

  resetForm(form: UtilisateurFormGroup, utilisateur: UtilisateurFormGroupInput): void {
    const utilisateurRawValue = { ...this.getFormDefaults(), ...utilisateur };
    form.reset(
      {
        ...utilisateurRawValue,
        id: { value: utilisateurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UtilisateurFormDefaults {
    return {
      id: null,
      notificationsPush: false,
    };
  }
}
