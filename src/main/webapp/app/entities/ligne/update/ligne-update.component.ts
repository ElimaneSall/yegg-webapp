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
import { LineStatus } from 'app/entities/enumerations/line-status.model';
import { LigneService } from '../service/ligne.service';
import { ILigne } from '../ligne.model';
import { LigneFormGroup, LigneFormService } from './ligne-form.service';

@Component({
  selector: 'jhi-ligne-update',
  templateUrl: './ligne-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LigneUpdateComponent implements OnInit {
  isSaving = false;
  ligne: ILigne | null = null;
  lineStatusValues = Object.keys(LineStatus);

  operateursSharedCollection: IOperateur[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected ligneService = inject(LigneService);
  protected ligneFormService = inject(LigneFormService);
  protected operateurService = inject(OperateurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LigneFormGroup = this.ligneFormService.createLigneFormGroup();

  compareOperateur = (o1: IOperateur | null, o2: IOperateur | null): boolean => this.operateurService.compareOperateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligne }) => {
      this.ligne = ligne;
      if (ligne) {
        this.updateForm(ligne);
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
    const ligne = this.ligneFormService.getLigne(this.editForm);
    if (ligne.id !== null) {
      this.subscribeToSaveResponse(this.ligneService.update(ligne));
    } else {
      this.subscribeToSaveResponse(this.ligneService.create(ligne));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILigne>>): void {
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

  protected updateForm(ligne: ILigne): void {
    this.ligne = ligne;
    this.ligneFormService.resetForm(this.editForm, ligne);

    this.operateursSharedCollection = this.operateurService.addOperateurToCollectionIfMissing<IOperateur>(
      this.operateursSharedCollection,
      ligne.operateur,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.operateurService
      .query()
      .pipe(map((res: HttpResponse<IOperateur[]>) => res.body ?? []))
      .pipe(
        map((operateurs: IOperateur[]) =>
          this.operateurService.addOperateurToCollectionIfMissing<IOperateur>(operateurs, this.ligne?.operateur),
        ),
      )
      .subscribe((operateurs: IOperateur[]) => (this.operateursSharedCollection = operateurs));
  }
}
