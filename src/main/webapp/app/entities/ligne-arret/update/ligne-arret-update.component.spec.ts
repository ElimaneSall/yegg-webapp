import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ILigne } from 'app/entities/ligne/ligne.model';
import { LigneService } from 'app/entities/ligne/service/ligne.service';
import { IArret } from 'app/entities/arret/arret.model';
import { ArretService } from 'app/entities/arret/service/arret.service';
import { ILigneArret } from '../ligne-arret.model';
import { LigneArretService } from '../service/ligne-arret.service';
import { LigneArretFormService } from './ligne-arret-form.service';

import { LigneArretUpdateComponent } from './ligne-arret-update.component';

describe('LigneArret Management Update Component', () => {
  let comp: LigneArretUpdateComponent;
  let fixture: ComponentFixture<LigneArretUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ligneArretFormService: LigneArretFormService;
  let ligneArretService: LigneArretService;
  let ligneService: LigneService;
  let arretService: ArretService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LigneArretUpdateComponent],
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
      .overrideTemplate(LigneArretUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LigneArretUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ligneArretFormService = TestBed.inject(LigneArretFormService);
    ligneArretService = TestBed.inject(LigneArretService);
    ligneService = TestBed.inject(LigneService);
    arretService = TestBed.inject(ArretService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Ligne query and add missing value', () => {
      const ligneArret: ILigneArret = { id: 30257 };
      const ligne: ILigne = { id: 18806 };
      ligneArret.ligne = ligne;

      const ligneCollection: ILigne[] = [{ id: 18806 }];
      jest.spyOn(ligneService, 'query').mockReturnValue(of(new HttpResponse({ body: ligneCollection })));
      const additionalLignes = [ligne];
      const expectedCollection: ILigne[] = [...additionalLignes, ...ligneCollection];
      jest.spyOn(ligneService, 'addLigneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ligneArret });
      comp.ngOnInit();

      expect(ligneService.query).toHaveBeenCalled();
      expect(ligneService.addLigneToCollectionIfMissing).toHaveBeenCalledWith(
        ligneCollection,
        ...additionalLignes.map(expect.objectContaining),
      );
      expect(comp.lignesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Arret query and add missing value', () => {
      const ligneArret: ILigneArret = { id: 30257 };
      const arret: IArret = { id: 23487 };
      ligneArret.arret = arret;

      const arretCollection: IArret[] = [{ id: 23487 }];
      jest.spyOn(arretService, 'query').mockReturnValue(of(new HttpResponse({ body: arretCollection })));
      const additionalArrets = [arret];
      const expectedCollection: IArret[] = [...additionalArrets, ...arretCollection];
      jest.spyOn(arretService, 'addArretToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ligneArret });
      comp.ngOnInit();

      expect(arretService.query).toHaveBeenCalled();
      expect(arretService.addArretToCollectionIfMissing).toHaveBeenCalledWith(
        arretCollection,
        ...additionalArrets.map(expect.objectContaining),
      );
      expect(comp.arretsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const ligneArret: ILigneArret = { id: 30257 };
      const ligne: ILigne = { id: 18806 };
      ligneArret.ligne = ligne;
      const arret: IArret = { id: 23487 };
      ligneArret.arret = arret;

      activatedRoute.data = of({ ligneArret });
      comp.ngOnInit();

      expect(comp.lignesSharedCollection).toContainEqual(ligne);
      expect(comp.arretsSharedCollection).toContainEqual(arret);
      expect(comp.ligneArret).toEqual(ligneArret);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILigneArret>>();
      const ligneArret = { id: 24214 };
      jest.spyOn(ligneArretFormService, 'getLigneArret').mockReturnValue(ligneArret);
      jest.spyOn(ligneArretService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ligneArret });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ligneArret }));
      saveSubject.complete();

      // THEN
      expect(ligneArretFormService.getLigneArret).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ligneArretService.update).toHaveBeenCalledWith(expect.objectContaining(ligneArret));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILigneArret>>();
      const ligneArret = { id: 24214 };
      jest.spyOn(ligneArretFormService, 'getLigneArret').mockReturnValue({ id: null });
      jest.spyOn(ligneArretService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ligneArret: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ligneArret }));
      saveSubject.complete();

      // THEN
      expect(ligneArretFormService.getLigneArret).toHaveBeenCalled();
      expect(ligneArretService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILigneArret>>();
      const ligneArret = { id: 24214 };
      jest.spyOn(ligneArretService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ligneArret });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ligneArretService.update).toHaveBeenCalled();
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

    describe('compareArret', () => {
      it('should forward to arretService', () => {
        const entity = { id: 23487 };
        const entity2 = { id: 26109 };
        jest.spyOn(arretService, 'compareArret');
        comp.compareArret(entity, entity2);
        expect(arretService.compareArret).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
