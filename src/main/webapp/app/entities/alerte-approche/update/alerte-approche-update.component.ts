import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { ThresholdType } from 'app/entities/enumerations/threshold-type.model';
import { AlertStatus } from 'app/entities/enumerations/alert-status.model';
import { AlerteApprocheService } from '../service/alerte-approche.service';
import { IAlerteApproche } from '../alerte-approche.model';
import { AlerteApprocheFormGroup, AlerteApprocheFormService } from './alerte-approche-form.service';

@Component({
  selector: 'jhi-alerte-approche-update',
  templateUrl: './alerte-approche-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AlerteApprocheUpdateComponent implements OnInit {
  isSaving = false;
  alerteApproche: IAlerteApproche | null = null;
  thresholdTypeValues = Object.keys(ThresholdType);
  alertStatusValues = Object.keys(AlertStatus);

  utilisateursSharedCollection: IUtilisateur[] = [];

  protected alerteApprocheService = inject(AlerteApprocheService);
  protected alerteApprocheFormService = inject(AlerteApprocheFormService);
  protected utilisateurService = inject(UtilisateurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AlerteApprocheFormGroup = this.alerteApprocheFormService.createAlerteApprocheFormGroup();

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alerteApproche }) => {
      this.alerteApproche = alerteApproche;
      if (alerteApproche) {
        this.updateForm(alerteApproche);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alerteApproche = this.alerteApprocheFormService.getAlerteApproche(this.editForm);
    if (alerteApproche.id !== null) {
      this.subscribeToSaveResponse(this.alerteApprocheService.update(alerteApproche));
    } else {
      this.subscribeToSaveResponse(this.alerteApprocheService.create(alerteApproche));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlerteApproche>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(alerteApproche: IAlerteApproche): void {
    this.alerteApproche = alerteApproche;
    this.alerteApprocheFormService.resetForm(this.editForm, alerteApproche);

    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(
      this.utilisateursSharedCollection,
      alerteApproche.utilisateur,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.alerteApproche?.utilisateur),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }
}
