import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBus } from 'app/entities/bus/bus.model';
import { BusService } from 'app/entities/bus/service/bus.service';
import { IAlerteApproche } from 'app/entities/alerte-approche/alerte-approche.model';
import { AlerteApprocheService } from 'app/entities/alerte-approche/service/alerte-approche.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { ThresholdType } from 'app/entities/enumerations/threshold-type.model';
import { HistoriqueAlerteService } from '../service/historique-alerte.service';
import { IHistoriqueAlerte } from '../historique-alerte.model';
import { HistoriqueAlerteFormGroup, HistoriqueAlerteFormService } from './historique-alerte-form.service';

@Component({
  selector: 'jhi-historique-alerte-update',
  templateUrl: './historique-alerte-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HistoriqueAlerteUpdateComponent implements OnInit {
  isSaving = false;
  historiqueAlerte: IHistoriqueAlerte | null = null;
  thresholdTypeValues = Object.keys(ThresholdType);

  busesSharedCollection: IBus[] = [];
  alerteApprochesSharedCollection: IAlerteApproche[] = [];
  utilisateursSharedCollection: IUtilisateur[] = [];

  protected historiqueAlerteService = inject(HistoriqueAlerteService);
  protected historiqueAlerteFormService = inject(HistoriqueAlerteFormService);
  protected busService = inject(BusService);
  protected alerteApprocheService = inject(AlerteApprocheService);
  protected utilisateurService = inject(UtilisateurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HistoriqueAlerteFormGroup = this.historiqueAlerteFormService.createHistoriqueAlerteFormGroup();

  compareBus = (o1: IBus | null, o2: IBus | null): boolean => this.busService.compareBus(o1, o2);

  compareAlerteApproche = (o1: IAlerteApproche | null, o2: IAlerteApproche | null): boolean =>
    this.alerteApprocheService.compareAlerteApproche(o1, o2);

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historiqueAlerte }) => {
      this.historiqueAlerte = historiqueAlerte;
      if (historiqueAlerte) {
        this.updateForm(historiqueAlerte);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const historiqueAlerte = this.historiqueAlerteFormService.getHistoriqueAlerte(this.editForm);
    if (historiqueAlerte.id !== null) {
      this.subscribeToSaveResponse(this.historiqueAlerteService.update(historiqueAlerte));
    } else {
      this.subscribeToSaveResponse(this.historiqueAlerteService.create(historiqueAlerte));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHistoriqueAlerte>>): void {
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

  protected updateForm(historiqueAlerte: IHistoriqueAlerte): void {
    this.historiqueAlerte = historiqueAlerte;
    this.historiqueAlerteFormService.resetForm(this.editForm, historiqueAlerte);

    this.busesSharedCollection = this.busService.addBusToCollectionIfMissing<IBus>(this.busesSharedCollection, historiqueAlerte.bus);
    this.alerteApprochesSharedCollection = this.alerteApprocheService.addAlerteApprocheToCollectionIfMissing<IAlerteApproche>(
      this.alerteApprochesSharedCollection,
      historiqueAlerte.alerteApproche,
    );
    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(
      this.utilisateursSharedCollection,
      historiqueAlerte.utilisateur,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.busService
      .query()
      .pipe(map((res: HttpResponse<IBus[]>) => res.body ?? []))
      .pipe(map((buses: IBus[]) => this.busService.addBusToCollectionIfMissing<IBus>(buses, this.historiqueAlerte?.bus)))
      .subscribe((buses: IBus[]) => (this.busesSharedCollection = buses));

    this.alerteApprocheService
      .query()
      .pipe(map((res: HttpResponse<IAlerteApproche[]>) => res.body ?? []))
      .pipe(
        map((alerteApproches: IAlerteApproche[]) =>
          this.alerteApprocheService.addAlerteApprocheToCollectionIfMissing<IAlerteApproche>(
            alerteApproches,
            this.historiqueAlerte?.alerteApproche,
          ),
        ),
      )
      .subscribe((alerteApproches: IAlerteApproche[]) => (this.alerteApprochesSharedCollection = alerteApproches));

    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.historiqueAlerte?.utilisateur),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }
}
