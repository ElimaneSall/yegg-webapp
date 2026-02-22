import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOperateur } from 'app/entities/operateur/operateur.model';
import { OperateurService } from 'app/entities/operateur/service/operateur.service';
import { ILigne } from '../ligne.model';
import { LigneService } from '../service/ligne.service';
import { LigneFormGroup, LigneFormService } from './ligne-form.service';

@Component({
  selector: 'jhi-ligne-update',
  templateUrl: './ligne-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LigneUpdateComponent implements OnInit {
  isSaving = false;
  ligne: ILigne | null = null;

  operateursSharedCollection: IOperateur[] = [];

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
