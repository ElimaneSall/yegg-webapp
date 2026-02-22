import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { ILigne } from 'app/entities/ligne/ligne.model';
import { LigneService } from 'app/entities/ligne/service/ligne.service';
import { BusService } from '../service/bus.service';
import { IBus } from '../bus.model';
import { BusFormGroup, BusFormService } from './bus-form.service';

@Component({
  selector: 'jhi-bus-update',
  templateUrl: './bus-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BusUpdateComponent implements OnInit {
  isSaving = false;
  bus: IBus | null = null;

  utilisateursCollection: IUtilisateur[] = [];
  lignesSharedCollection: ILigne[] = [];

  protected busService = inject(BusService);
  protected busFormService = inject(BusFormService);
  protected utilisateurService = inject(UtilisateurService);
  protected ligneService = inject(LigneService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BusFormGroup = this.busFormService.createBusFormGroup();

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  compareLigne = (o1: ILigne | null, o2: ILigne | null): boolean => this.ligneService.compareLigne(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bus }) => {
      this.bus = bus;
      if (bus) {
        this.updateForm(bus);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bus = this.busFormService.getBus(this.editForm);
    if (bus.id !== null) {
      this.subscribeToSaveResponse(this.busService.update(bus));
    } else {
      this.subscribeToSaveResponse(this.busService.create(bus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBus>>): void {
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

  protected updateForm(bus: IBus): void {
    this.bus = bus;
    this.busFormService.resetForm(this.editForm, bus);

    this.utilisateursCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(
      this.utilisateursCollection,
      bus.utilisateur,
    );
    this.lignesSharedCollection = this.ligneService.addLigneToCollectionIfMissing<ILigne>(this.lignesSharedCollection, bus.ligne);
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query({ 'busId.specified': 'false' })
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.bus?.utilisateur),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursCollection = utilisateurs));

    this.ligneService
      .query()
      .pipe(map((res: HttpResponse<ILigne[]>) => res.body ?? []))
      .pipe(map((lignes: ILigne[]) => this.ligneService.addLigneToCollectionIfMissing<ILigne>(lignes, this.bus?.ligne)))
      .subscribe((lignes: ILigne[]) => (this.lignesSharedCollection = lignes));
  }
}
