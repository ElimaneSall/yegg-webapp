import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFeedback, NewFeedback } from '../feedback.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedback for edit and NewFeedbackFormGroupInput for create.
 */
type FeedbackFormGroupInput = IFeedback | PartialWithRequiredKeyOf<NewFeedback>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFeedback | NewFeedback> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

type FeedbackFormRawValue = FormValueOf<IFeedback>;

type NewFeedbackFormRawValue = FormValueOf<NewFeedback>;

type FeedbackFormDefaults = Pick<NewFeedback, 'id' | 'dateCreation' | 'anonyme'>;

type FeedbackFormGroupContent = {
  id: FormControl<FeedbackFormRawValue['id'] | NewFeedback['id']>;
  note: FormControl<FeedbackFormRawValue['note']>;
  commentaire: FormControl<FeedbackFormRawValue['commentaire']>;
  typeObjet: FormControl<FeedbackFormRawValue['typeObjet']>;
  objetId: FormControl<FeedbackFormRawValue['objetId']>;
  dateCreation: FormControl<FeedbackFormRawValue['dateCreation']>;
  anonyme: FormControl<FeedbackFormRawValue['anonyme']>;
  utilisateur: FormControl<FeedbackFormRawValue['utilisateur']>;
};

export type FeedbackFormGroup = FormGroup<FeedbackFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackFormService {
  createFeedbackFormGroup(feedback: FeedbackFormGroupInput = { id: null }): FeedbackFormGroup {
    const feedbackRawValue = this.convertFeedbackToFeedbackRawValue({
      ...this.getFormDefaults(),
      ...feedback,
    });
    return new FormGroup<FeedbackFormGroupContent>({
      id: new FormControl(
        { value: feedbackRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      note: new FormControl(feedbackRawValue.note, {
        validators: [Validators.required, Validators.min(1), Validators.max(5)],
      }),
      commentaire: new FormControl(feedbackRawValue.commentaire),
      typeObjet: new FormControl(feedbackRawValue.typeObjet, {
        validators: [Validators.required],
      }),
      objetId: new FormControl(feedbackRawValue.objetId, {
        validators: [Validators.required],
      }),
      dateCreation: new FormControl(feedbackRawValue.dateCreation, {
        validators: [Validators.required],
      }),
      anonyme: new FormControl(feedbackRawValue.anonyme),
      utilisateur: new FormControl(feedbackRawValue.utilisateur),
    });
  }

  getFeedback(form: FeedbackFormGroup): IFeedback | NewFeedback {
    return this.convertFeedbackRawValueToFeedback(form.getRawValue() as FeedbackFormRawValue | NewFeedbackFormRawValue);
  }

  resetForm(form: FeedbackFormGroup, feedback: FeedbackFormGroupInput): void {
    const feedbackRawValue = this.convertFeedbackToFeedbackRawValue({ ...this.getFormDefaults(), ...feedback });
    form.reset(
      {
        ...feedbackRawValue,
        id: { value: feedbackRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FeedbackFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreation: currentTime,
      anonyme: false,
    };
  }

  private convertFeedbackRawValueToFeedback(rawFeedback: FeedbackFormRawValue | NewFeedbackFormRawValue): IFeedback | NewFeedback {
    return {
      ...rawFeedback,
      dateCreation: dayjs(rawFeedback.dateCreation, DATE_TIME_FORMAT),
    };
  }

  private convertFeedbackToFeedbackRawValue(
    feedback: IFeedback | (Partial<NewFeedback> & FeedbackFormDefaults),
  ): FeedbackFormRawValue | PartialWithRequiredKeyOf<NewFeedbackFormRawValue> {
    return {
      ...feedback,
      dateCreation: feedback.dateCreation ? feedback.dateCreation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
