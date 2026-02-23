import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ILigne } from 'app/entities/ligne/ligne.model';
import { LigneService } from 'app/entities/ligne/service/ligne.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IBus } from '../bus.model';
import { BusService } from '../service/bus.service';
import { BusFormService } from './bus-form.service';

import { BusUpdateComponent } from './bus-update.component';

describe('Bus Management Update Component', () => {
  let comp: BusUpdateComponent;
  let fixture: ComponentFixture<BusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let busFormService: BusFormService;
  let busService: BusService;
  let ligneService: LigneService;
  let utilisateurService: UtilisateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BusUpdateComponent],
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
      .overrideTemplate(BusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    busFormService = TestBed.inject(BusFormService);
    busService = TestBed.inject(BusService);
    ligneService = TestBed.inject(LigneService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Ligne query and add missing value', () => {
      const bus: IBus = { id: 1566 };
      const ligne: ILigne = { id: 18806 };
      bus.ligne = ligne;

      const ligneCollection: ILigne[] = [{ id: 18806 }];
      jest.spyOn(ligneService, 'query').mockReturnValue(of(new HttpResponse({ body: ligneCollection })));
      const additionalLignes = [ligne];
      const expectedCollection: ILigne[] = [...additionalLignes, ...ligneCollection];
      jest.spyOn(ligneService, 'addLigneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bus });
      comp.ngOnInit();

      expect(ligneService.query).toHaveBeenCalled();
      expect(ligneService.addLigneToCollectionIfMissing).toHaveBeenCalledWith(
        ligneCollection,
        ...additionalLignes.map(expect.objectContaining),
      );
      expect(comp.lignesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Utilisateur query and add missing value', () => {
      const bus: IBus = { id: 1566 };
      const chauffeur: IUtilisateur = { id: 2179 };
      bus.chauffeur = chauffeur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [chauffeur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bus });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(expect.objectContaining),
      );
      expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const bus: IBus = { id: 1566 };
      const ligne: ILigne = { id: 18806 };
      bus.ligne = ligne;
      const chauffeur: IUtilisateur = { id: 2179 };
      bus.chauffeur = chauffeur;

      activatedRoute.data = of({ bus });
      comp.ngOnInit();

      expect(comp.lignesSharedCollection).toContainEqual(ligne);
      expect(comp.utilisateursSharedCollection).toContainEqual(chauffeur);
      expect(comp.bus).toEqual(bus);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBus>>();
      const bus = { id: 26950 };
      jest.spyOn(busFormService, 'getBus').mockReturnValue(bus);
      jest.spyOn(busService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bus }));
      saveSubject.complete();

      // THEN
      expect(busFormService.getBus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(busService.update).toHaveBeenCalledWith(expect.objectContaining(bus));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBus>>();
      const bus = { id: 26950 };
      jest.spyOn(busFormService, 'getBus').mockReturnValue({ id: null });
      jest.spyOn(busService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bus }));
      saveSubject.complete();

      // THEN
      expect(busFormService.getBus).toHaveBeenCalled();
      expect(busService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBus>>();
      const bus = { id: 26950 };
      jest.spyOn(busService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(busService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLigne', () => {
      it('should forward to ligneService', () => {
        const entity = { id: 18806 };
        const entity2 = { id: 21722 };
        jest.spyOn(ligneService, 'compareLigne');
        comp.compareLigne(entity, entity2);
        expect(ligneService.compareLigne).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
