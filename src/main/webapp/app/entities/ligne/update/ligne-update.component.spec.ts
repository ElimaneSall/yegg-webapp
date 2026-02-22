import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOperateur } from 'app/entities/operateur/operateur.model';
import { OperateurService } from 'app/entities/operateur/service/operateur.service';
import { LigneService } from '../service/ligne.service';
import { ILigne } from '../ligne.model';
import { LigneFormService } from './ligne-form.service';

import { LigneUpdateComponent } from './ligne-update.component';

describe('Ligne Management Update Component', () => {
  let comp: LigneUpdateComponent;
  let fixture: ComponentFixture<LigneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ligneFormService: LigneFormService;
  let ligneService: LigneService;
  let operateurService: OperateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LigneUpdateComponent],
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
      .overrideTemplate(LigneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LigneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ligneFormService = TestBed.inject(LigneFormService);
    ligneService = TestBed.inject(LigneService);
    operateurService = TestBed.inject(OperateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Operateur query and add missing value', () => {
      const ligne: ILigne = { id: 21722 };
      const operateur: IOperateur = { id: 26029 };
      ligne.operateur = operateur;

      const operateurCollection: IOperateur[] = [{ id: 26029 }];
      jest.spyOn(operateurService, 'query').mockReturnValue(of(new HttpResponse({ body: operateurCollection })));
      const additionalOperateurs = [operateur];
      const expectedCollection: IOperateur[] = [...additionalOperateurs, ...operateurCollection];
      jest.spyOn(operateurService, 'addOperateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ligne });
      comp.ngOnInit();

      expect(operateurService.query).toHaveBeenCalled();
      expect(operateurService.addOperateurToCollectionIfMissing).toHaveBeenCalledWith(
        operateurCollection,
        ...additionalOperateurs.map(expect.objectContaining),
      );
      expect(comp.operateursSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const ligne: ILigne = { id: 21722 };
      const operateur: IOperateur = { id: 26029 };
      ligne.operateur = operateur;

      activatedRoute.data = of({ ligne });
      comp.ngOnInit();

      expect(comp.operateursSharedCollection).toContainEqual(operateur);
      expect(comp.ligne).toEqual(ligne);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILigne>>();
      const ligne = { id: 18806 };
      jest.spyOn(ligneFormService, 'getLigne').mockReturnValue(ligne);
      jest.spyOn(ligneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ligne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ligne }));
      saveSubject.complete();

      // THEN
      expect(ligneFormService.getLigne).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ligneService.update).toHaveBeenCalledWith(expect.objectContaining(ligne));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILigne>>();
      const ligne = { id: 18806 };
      jest.spyOn(ligneFormService, 'getLigne').mockReturnValue({ id: null });
      jest.spyOn(ligneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ligne: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ligne }));
      saveSubject.complete();

      // THEN
      expect(ligneFormService.getLigne).toHaveBeenCalled();
      expect(ligneService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILigne>>();
      const ligne = { id: 18806 };
      jest.spyOn(ligneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ligne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ligneService.update).toHaveBeenCalled();
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
  });
});
