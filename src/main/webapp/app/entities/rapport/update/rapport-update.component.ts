import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IOperateur } from 'app/entities/operateur/operateur.model';
import { OperateurService } from 'app/entities/operateur/service/operateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { ReportType } from 'app/entities/enumerations/report-type.model';
import { ReportFormat } from 'app/entities/enumerations/report-format.model';
import { RapportService } from '../service/rapport.service';
import { IRapport } from '../rapport.model';
import { RapportFormGroup, RapportFormService } from './rapport-form.service';

@Component({
  selector: 'jhi-rapport-update',
  templateUrl: './rapport-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RapportUpdateComponent implements OnInit {
  isSaving = false;
  rapport: IRapport | null = null;
  reportTypeValues = Object.keys(ReportType);
  reportFormatValues = Object.keys(ReportFormat);

  operateursSharedCollection: IOperateur[] = [];
  utilisateursSharedCollection: IUtilisateur[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected rapportService = inject(RapportService);
  protected rapportFormService = inject(RapportFormService);
  protected operateurService = inject(OperateurService);
  protected utilisateurService = inject(UtilisateurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RapportFormGroup = this.rapportFormService.createRapportFormGroup();

  compareOperateur = (o1: IOperateur | null, o2: IOperateur | null): boolean => this.operateurService.compareOperateur(o1, o2);

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rapport }) => {
      this.rapport = rapport;
      if (rapport) {
        this.updateForm(rapport);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('yeggApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rapport = this.rapportFormService.getRapport(this.editForm);
    if (rapport.id !== null) {
      this.subscribeToSaveResponse(this.rapportService.update(rapport));
    } else {
      this.subscribeToSaveResponse(this.rapportService.create(rapport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRapport>>): void {
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

  protected updateForm(rapport: IRapport): void {
    this.rapport = rapport;
    this.rapportFormService.resetForm(this.editForm, rapport);

    this.operateursSharedCollection = this.operateurService.addOperateurToCollectionIfMissing<IOperateur>(
      this.operateursSharedCollection,
      rapport.operateur,
    );
    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(
      this.utilisateursSharedCollection,
      rapport.admin,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.operateurService
      .query()
      .pipe(map((res: HttpResponse<IOperateur[]>) => res.body ?? []))
      .pipe(
        map((operateurs: IOperateur[]) =>
          this.operateurService.addOperateurToCollectionIfMissing<IOperateur>(operateurs, this.rapport?.operateur),
        ),
      )
      .subscribe((operateurs: IOperateur[]) => (this.operateursSharedCollection = operateurs));

    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.rapport?.admin),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }
}
