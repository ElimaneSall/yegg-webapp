import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ILigne } from 'app/entities/ligne/ligne.model';
import { LigneService } from 'app/entities/ligne/service/ligne.service';
import { IArret } from 'app/entities/arret/arret.model';
import { ArretService } from 'app/entities/arret/service/arret.service';
import { IAlerteApproche } from 'app/entities/alerte-approche/alerte-approche.model';
import { AlerteApprocheService } from 'app/entities/alerte-approche/service/alerte-approche.service';
import { AlerteLigneArretService } from '../service/alerte-ligne-arret.service';
import { IAlerteLigneArret } from '../alerte-ligne-arret.model';
import { AlerteLigneArretFormGroup, AlerteLigneArretFormService } from './alerte-ligne-arret-form.service';

@Component({
  selector: 'jhi-alerte-ligne-arret-update',
  templateUrl: './alerte-ligne-arret-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AlerteLigneArretUpdateComponent implements OnInit {
  isSaving = false;
  alerteLigneArret: IAlerteLigneArret | null = null;

  lignesSharedCollection: ILigne[] = [];
  arretsSharedCollection: IArret[] = [];
  alerteApprochesSharedCollection: IAlerteApproche[] = [];

  protected alerteLigneArretService = inject(AlerteLigneArretService);
  protected alerteLigneArretFormService = inject(AlerteLigneArretFormService);
  protected ligneService = inject(LigneService);
  protected arretService = inject(ArretService);
  protected alerteApprocheService = inject(AlerteApprocheService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AlerteLigneArretFormGroup = this.alerteLigneArretFormService.createAlerteLigneArretFormGroup();

  compareLigne = (o1: ILigne | null, o2: ILigne | null): boolean => this.ligneService.compareLigne(o1, o2);

  compareArret = (o1: IArret | null, o2: IArret | null): boolean => this.arretService.compareArret(o1, o2);

  compareAlerteApproche = (o1: IAlerteApproche | null, o2: IAlerteApproche | null): boolean =>
    this.alerteApprocheService.compareAlerteApproche(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alerteLigneArret }) => {
      this.alerteLigneArret = alerteLigneArret;
      if (alerteLigneArret) {
        this.updateForm(alerteLigneArret);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alerteLigneArret = this.alerteLigneArretFormService.getAlerteLigneArret(this.editForm);
    if (alerteLigneArret.id !== null) {
      this.subscribeToSaveResponse(this.alerteLigneArretService.update(alerteLigneArret));
    } else {
      this.subscribeToSaveResponse(this.alerteLigneArretService.create(alerteLigneArret));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlerteLigneArret>>): void {
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

  protected updateForm(alerteLigneArret: IAlerteLigneArret): void {
    this.alerteLigneArret = alerteLigneArret;
    this.alerteLigneArretFormService.resetForm(this.editForm, alerteLigneArret);

    this.lignesSharedCollection = this.ligneService.addLigneToCollectionIfMissing<ILigne>(
      this.lignesSharedCollection,
      alerteLigneArret.ligne,
    );
    this.arretsSharedCollection = this.arretService.addArretToCollectionIfMissing<IArret>(
      this.arretsSharedCollection,
      alerteLigneArret.arret,
    );
    this.alerteApprochesSharedCollection = this.alerteApprocheService.addAlerteApprocheToCollectionIfMissing<IAlerteApproche>(
      this.alerteApprochesSharedCollection,
      alerteLigneArret.alerteApproche,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ligneService
      .query()
      .pipe(map((res: HttpResponse<ILigne[]>) => res.body ?? []))
      .pipe(map((lignes: ILigne[]) => this.ligneService.addLigneToCollectionIfMissing<ILigne>(lignes, this.alerteLigneArret?.ligne)))
      .subscribe((lignes: ILigne[]) => (this.lignesSharedCollection = lignes));

    this.arretService
      .query()
      .pipe(map((res: HttpResponse<IArret[]>) => res.body ?? []))
      .pipe(map((arrets: IArret[]) => this.arretService.addArretToCollectionIfMissing<IArret>(arrets, this.alerteLigneArret?.arret)))
      .subscribe((arrets: IArret[]) => (this.arretsSharedCollection = arrets));

    this.alerteApprocheService
      .query()
      .pipe(map((res: HttpResponse<IAlerteApproche[]>) => res.body ?? []))
      .pipe(
        map((alerteApproches: IAlerteApproche[]) =>
          this.alerteApprocheService.addAlerteApprocheToCollectionIfMissing<IAlerteApproche>(
            alerteApproches,
            this.alerteLigneArret?.alerteApproche,
          ),
        ),
      )
      .subscribe((alerteApproches: IAlerteApproche[]) => (this.alerteApprochesSharedCollection = alerteApproches));
  }
}
