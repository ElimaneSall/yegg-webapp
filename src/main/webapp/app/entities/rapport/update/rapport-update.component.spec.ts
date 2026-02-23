import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOperateur } from 'app/entities/operateur/operateur.model';
import { OperateurService } from 'app/entities/operateur/service/operateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IRapport } from '../rapport.model';
import { RapportService } from '../service/rapport.service';
import { RapportFormService } from './rapport-form.service';

import { RapportUpdateComponent } from './rapport-update.component';

describe('Rapport Management Update Component', () => {
  let comp: RapportUpdateComponent;
  let fixture: ComponentFixture<RapportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rapportFormService: RapportFormService;
  let rapportService: RapportService;
  let operateurService: OperateurService;
  let utilisateurService: UtilisateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RapportUpdateComponent],
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
      .overrideTemplate(RapportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RapportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rapportFormService = TestBed.inject(RapportFormService);
    rapportService = TestBed.inject(RapportService);
    operateurService = TestBed.inject(OperateurService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Operateur query and add missing value', () => {
      const rapport: IRapport = { id: 7086 };
      const operateur: IOperateur = { id: 26029 };
      rapport.operateur = operateur;

      const operateurCollection: IOperateur[] = [{ id: 26029 }];
      jest.spyOn(operateurService, 'query').mockReturnValue(of(new HttpResponse({ body: operateurCollection })));
      const additionalOperateurs = [operateur];
      const expectedCollection: IOperateur[] = [...additionalOperateurs, ...operateurCollection];
      jest.spyOn(operateurService, 'addOperateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rapport });
      comp.ngOnInit();

      expect(operateurService.query).toHaveBeenCalled();
      expect(operateurService.addOperateurToCollectionIfMissing).toHaveBeenCalledWith(
        operateurCollection,
        ...additionalOperateurs.map(expect.objectContaining),
      );
      expect(comp.operateursSharedCollection).toEqual(expectedCollection);
    });

    it('should call Utilisateur query and add missing value', () => {
      const rapport: IRapport = { id: 7086 };
      const admin: IUtilisateur = { id: 2179 };
      rapport.admin = admin;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [admin];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rapport });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(expect.objectContaining),
      );
      expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const rapport: IRapport = { id: 7086 };
      const operateur: IOperateur = { id: 26029 };
      rapport.operateur = operateur;
      const admin: IUtilisateur = { id: 2179 };
      rapport.admin = admin;

      activatedRoute.data = of({ rapport });
      comp.ngOnInit();

      expect(comp.operateursSharedCollection).toContainEqual(operateur);
      expect(comp.utilisateursSharedCollection).toContainEqual(admin);
      expect(comp.rapport).toEqual(rapport);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRapport>>();
      const rapport = { id: 19730 };
      jest.spyOn(rapportFormService, 'getRapport').mockReturnValue(rapport);
      jest.spyOn(rapportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rapport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rapport }));
      saveSubject.complete();

      // THEN
      expect(rapportFormService.getRapport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rapportService.update).toHaveBeenCalledWith(expect.objectContaining(rapport));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRapport>>();
      const rapport = { id: 19730 };
      jest.spyOn(rapportFormService, 'getRapport').mockReturnValue({ id: null });
      jest.spyOn(rapportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rapport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rapport }));
      saveSubject.complete();

      // THEN
      expect(rapportFormService.getRapport).toHaveBeenCalled();
      expect(rapportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRapport>>();
      const rapport = { id: 19730 };
      jest.spyOn(rapportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rapport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rapportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOperateur', () => {
      it('should forward to operateurService', () => {
        const entity = { id: 26029 };
        const entity2 = { id: 31481 };
        jest.spyOn(operateurService, 'compareOperateur');
        comp.compareOperateur(entity, entity2);
        expect(operateurService.compareOperateur).toHaveBeenCalledWith(entity, entity2);
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
