import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IFavori } from '../favori.model';
import { FavoriService } from '../service/favori.service';
import { FavoriFormGroup, FavoriFormService } from './favori-form.service';

@Component({
  selector: 'jhi-favori-update',
  templateUrl: './favori-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FavoriUpdateComponent implements OnInit {
  isSaving = false;
  favori: IFavori | null = null;

  utilisateursSharedCollection: IUtilisateur[] = [];

  protected favoriService = inject(FavoriService);
  protected favoriFormService = inject(FavoriFormService);
  protected utilisateurService = inject(UtilisateurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FavoriFormGroup = this.favoriFormService.createFavoriFormGroup();

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ favori }) => {
      this.favori = favori;
      if (favori) {
        this.updateForm(favori);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const favori = this.favoriFormService.getFavori(this.editForm);
    if (favori.id !== null) {
      this.subscribeToSaveResponse(this.favoriService.update(favori));
    } else {
      this.subscribeToSaveResponse(this.favoriService.create(favori));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFavori>>): void {
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

  protected updateForm(favori: IFavori): void {
    this.favori = favori;
    this.favoriFormService.resetForm(this.editForm, favori);

    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(
      this.utilisateursSharedCollection,
      favori.utilisateur,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.favori?.utilisateur),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }
}
