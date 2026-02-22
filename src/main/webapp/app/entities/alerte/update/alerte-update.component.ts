import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IAlerte } from '../alerte.model';
import { AlerteService } from '../service/alerte.service';
import { AlerteFormGroup, AlerteFormService } from './alerte-form.service';

@Component({
  selector: 'jhi-alerte-update',
  templateUrl: './alerte-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AlerteUpdateComponent implements OnInit {
  isSaving = false;
  alerte: IAlerte | null = null;

  utilisateursSharedCollection: IUtilisateur[] = [];

  protected alerteService = inject(AlerteService);
  protected alerteFormService = inject(AlerteFormService);
  protected utilisateurService = inject(UtilisateurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AlerteFormGroup = this.alerteFormService.createAlerteFormGroup();

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alerte }) => {
      this.alerte = alerte;
      if (alerte) {
        this.updateForm(alerte);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alerte = this.alerteFormService.getAlerte(this.editForm);
    if (alerte.id !== null) {
      this.subscribeToSaveResponse(this.alerteService.update(alerte));
    } else {
      this.subscribeToSaveResponse(this.alerteService.create(alerte));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlerte>>): void {
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

  protected updateForm(alerte: IAlerte): void {
    this.alerte = alerte;
    this.alerteFormService.resetForm(this.editForm, alerte);

    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(
      this.utilisateursSharedCollection,
      alerte.utilisateur,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.alerte?.utilisateur),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }
}
