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
import { LigneArretService } from '../service/ligne-arret.service';
import { ILigneArret } from '../ligne-arret.model';
import { LigneArretFormGroup, LigneArretFormService } from './ligne-arret-form.service';

@Component({
  selector: 'jhi-ligne-arret-update',
  templateUrl: './ligne-arret-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LigneArretUpdateComponent implements OnInit {
  isSaving = false;
  ligneArret: ILigneArret | null = null;

  lignesSharedCollection: ILigne[] = [];
  arretsSharedCollection: IArret[] = [];

  protected ligneArretService = inject(LigneArretService);
  protected ligneArretFormService = inject(LigneArretFormService);
  protected ligneService = inject(LigneService);
  protected arretService = inject(ArretService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LigneArretFormGroup = this.ligneArretFormService.createLigneArretFormGroup();

  compareLigne = (o1: ILigne | null, o2: ILigne | null): boolean => this.ligneService.compareLigne(o1, o2);

  compareArret = (o1: IArret | null, o2: IArret | null): boolean => this.arretService.compareArret(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligneArret }) => {
      this.ligneArret = ligneArret;
      if (ligneArret) {
        this.updateForm(ligneArret);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ligneArret = this.ligneArretFormService.getLigneArret(this.editForm);
    if (ligneArret.id !== null) {
      this.subscribeToSaveResponse(this.ligneArretService.update(ligneArret));
    } else {
      this.subscribeToSaveResponse(this.ligneArretService.create(ligneArret));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILigneArret>>): void {
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

  protected updateForm(ligneArret: ILigneArret): void {
    this.ligneArret = ligneArret;
    this.ligneArretFormService.resetForm(this.editForm, ligneArret);

    this.lignesSharedCollection = this.ligneService.addLigneToCollectionIfMissing<ILigne>(this.lignesSharedCollection, ligneArret.ligne);
    this.arretsSharedCollection = this.arretService.addArretToCollectionIfMissing<IArret>(this.arretsSharedCollection, ligneArret.arret);
  }

  protected loadRelationshipsOptions(): void {
    this.ligneService
      .query()
      .pipe(map((res: HttpResponse<ILigne[]>) => res.body ?? []))
      .pipe(map((lignes: ILigne[]) => this.ligneService.addLigneToCollectionIfMissing<ILigne>(lignes, this.ligneArret?.ligne)))
      .subscribe((lignes: ILigne[]) => (this.lignesSharedCollection = lignes));

    this.arretService
      .query()
      .pipe(map((res: HttpResponse<IArret[]>) => res.body ?? []))
      .pipe(map((arrets: IArret[]) => this.arretService.addArretToCollectionIfMissing<IArret>(arrets, this.ligneArret?.arret)))
      .subscribe((arrets: IArret[]) => (this.arretsSharedCollection = arrets));
  }
}
