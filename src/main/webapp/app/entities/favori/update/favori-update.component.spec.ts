import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { FavoriService } from '../service/favori.service';
import { IFavori } from '../favori.model';
import { FavoriFormService } from './favori-form.service';

import { FavoriUpdateComponent } from './favori-update.component';

describe('Favori Management Update Component', () => {
  let comp: FavoriUpdateComponent;
  let fixture: ComponentFixture<FavoriUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let favoriFormService: FavoriFormService;
  let favoriService: FavoriService;
  let utilisateurService: UtilisateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FavoriUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FavoriUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FavoriUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    favoriFormService = TestBed.inject(FavoriFormService);
    favoriService = TestBed.inject(FavoriService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Utilisateur query and add missing value', () => {
      const favori: IFavori = { id: 28168 };
      const utilisateur: IUtilisateur = { id: 2179 };
      favori.utilisateur = utilisateur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [utilisateur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ favori });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(expect.objectContaining),
      );
      expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const favori: IFavori = { id: 28168 };
      const utilisateur: IUtilisateur = { id: 2179 };
      favori.utilisateur = utilisateur;

      activatedRoute.data = of({ favori });
      comp.ngOnInit();

      expect(comp.utilisateursSharedCollection).toContainEqual(utilisateur);
      expect(comp.favori).toEqual(favori);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFavori>>();
      const favori = { id: 20334 };
      jest.spyOn(favoriFormService, 'getFavori').mockReturnValue(favori);
      jest.spyOn(favoriService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ favori });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: favori }));
      saveSubject.complete();

      // THEN
      expect(favoriFormService.getFavori).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(favoriService.update).toHaveBeenCalledWith(expect.objectContaining(favori));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFavori>>();
      const favori = { id: 20334 };
      jest.spyOn(favoriFormService, 'getFavori').mockReturnValue({ id: null });
      jest.spyOn(favoriService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ favori: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: favori }));
      saveSubject.complete();

      // THEN
      expect(favoriFormService.getFavori).toHaveBeenCalled();
      expect(favoriService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFavori>>();
      const favori = { id: 20334 };
      jest.spyOn(favoriService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ favori });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(favoriService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUtilisateur', () => {
      it('should forward to utilisateurService', () => {
        const entity = { id: 2179 };
        const entity2 = { id: 31928 };
        jest.spyOn(utilisateurService, 'compareUtilisateur');
        comp.compareUtilisateur(entity, entity2);
        expect(utilisateurService.compareUtilisateur).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
