import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ILigne } from 'app/entities/ligne/ligne.model';
import { LigneService } from 'app/entities/ligne/service/ligne.service';
import { IArret } from 'app/entities/arret/arret.model';
import { ArretService } from 'app/entities/arret/service/arret.service';
import { IAlerteApproche } from 'app/entities/alerte-approche/alerte-approche.model';
import { AlerteApprocheService } from 'app/entities/alerte-approche/service/alerte-approche.service';
import { IAlerteLigneArret } from '../alerte-ligne-arret.model';
import { AlerteLigneArretService } from '../service/alerte-ligne-arret.service';
import { AlerteLigneArretFormService } from './alerte-ligne-arret-form.service';

import { AlerteLigneArretUpdateComponent } from './alerte-ligne-arret-update.component';

describe('AlerteLigneArret Management Update Component', () => {
  let comp: AlerteLigneArretUpdateComponent;
  let fixture: ComponentFixture<AlerteLigneArretUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alerteLigneArretFormService: AlerteLigneArretFormService;
  let alerteLigneArretService: AlerteLigneArretService;
  let ligneService: LigneService;
  let arretService: ArretService;
  let alerteApprocheService: AlerteApprocheService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AlerteLigneArretUpdateComponent],
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
      .overrideTemplate(AlerteLigneArretUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlerteLigneArretUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alerteLigneArretFormService = TestBed.inject(AlerteLigneArretFormService);
    alerteLigneArretService = TestBed.inject(AlerteLigneArretService);
    ligneService = TestBed.inject(LigneService);
    arretService = TestBed.inject(ArretService);
    alerteApprocheService = TestBed.inject(AlerteApprocheService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Ligne query and add missing value', () => {
      const alerteLigneArret: IAlerteLigneArret = { id: 11413 };
      const ligne: ILigne = { id: 18806 };
      alerteLigneArret.ligne = ligne;

      const ligneCollection: ILigne[] = [{ id: 18806 }];
      jest.spyOn(ligneService, 'query').mockReturnValue(of(new HttpResponse({ body: ligneCollection })));
      const additionalLignes = [ligne];
      const expectedCollection: ILigne[] = [...additionalLignes, ...ligneCollection];
      jest.spyOn(ligneService, 'addLigneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alerteLigneArret });
      comp.ngOnInit();

      expect(ligneService.query).toHaveBeenCalled();
      expect(ligneService.addLigneToCollectionIfMissing).toHaveBeenCalledWith(
        ligneCollection,
        ...additionalLignes.map(expect.objectContaining),
      );
      expect(comp.lignesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Arret query and add missing value', () => {
      const alerteLigneArret: IAlerteLigneArret = { id: 11413 };
      const arret: IArret = { id: 23487 };
      alerteLigneArret.arret = arret;

      const arretCollection: IArret[] = [{ id: 23487 }];
      jest.spyOn(arretService, 'query').mockReturnValue(of(new HttpResponse({ body: arretCollection })));
      const additionalArrets = [arret];
      const expectedCollection: IArret[] = [...additionalArrets, ...arretCollection];
      jest.spyOn(arretService, 'addArretToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alerteLigneArret });
      comp.ngOnInit();

      expect(arretService.query).toHaveBeenCalled();
      expect(arretService.addArretToCollectionIfMissing).toHaveBeenCalledWith(
        arretCollection,
        ...additionalArrets.map(expect.objectContaining),
      );
      expect(comp.arretsSharedCollection).toEqual(expectedCollection);
    });

    it('should call AlerteApproche query and add missing value', () => {
      const alerteLigneArret: IAlerteLigneArret = { id: 11413 };
      const alerteApproche: IAlerteApproche = { id: 19175 };
      alerteLigneArret.alerteApproche = alerteApproche;

      const alerteApprocheCollection: IAlerteApproche[] = [{ id: 19175 }];
      jest.spyOn(alerteApprocheService, 'query').mockReturnValue(of(new HttpResponse({ body: alerteApprocheCollection })));
      const additionalAlerteApproches = [alerteApproche];
      const expectedCollection: IAlerteApproche[] = [...additionalAlerteApproches, ...alerteApprocheCollection];
      jest.spyOn(alerteApprocheService, 'addAlerteApprocheToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alerteLigneArret });
      comp.ngOnInit();

      expect(alerteApprocheService.query).toHaveBeenCalled();
      expect(alerteApprocheService.addAlerteApprocheToCollectionIfMissing).toHaveBeenCalledWith(
        alerteApprocheCollection,
        ...additionalAlerteApproches.map(expect.objectContaining),
      );
      expect(comp.alerteApprochesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const alerteLigneArret: IAlerteLigneArret = { id: 11413 };
      const ligne: ILigne = { id: 18806 };
      alerteLigneArret.ligne = ligne;
      const arret: IArret = { id: 23487 };
      alerteLigneArret.arret = arret;
      const alerteApproche: IAlerteApproche = { id: 19175 };
      alerteLigneArret.alerteApproche = alerteApproche;

      activatedRoute.data = of({ alerteLigneArret });
      comp.ngOnInit();

      expect(comp.lignesSharedCollection).toContainEqual(ligne);
      expect(comp.arretsSharedCollection).toContainEqual(arret);
      expect(comp.alerteApprochesSharedCollection).toContainEqual(alerteApproche);
      expect(comp.alerteLigneArret).toEqual(alerteLigneArret);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlerteLigneArret>>();
      const alerteLigneArret = { id: 29289 };
      jest.spyOn(alerteLigneArretFormService, 'getAlerteLigneArret').mockReturnValue(alerteLigneArret);
      jest.spyOn(alerteLigneArretService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alerteLigneArret });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alerteLigneArret }));
      saveSubject.complete();

      // THEN
      expect(alerteLigneArretFormService.getAlerteLigneArret).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alerteLigneArretService.update).toHaveBeenCalledWith(expect.objectContaining(alerteLigneArret));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlerteLigneArret>>();
      const alerteLigneArret = { id: 29289 };
      jest.spyOn(alerteLigneArretFormService, 'getAlerteLigneArret').mockReturnValue({ id: null });
      jest.spyOn(alerteLigneArretService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alerteLigneArret: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alerteLigneArret }));
      saveSubject.complete();

      // THEN
      expect(alerteLigneArretFormService.getAlerteLigneArret).toHaveBeenCalled();
      expect(alerteLigneArretService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlerteLigneArret>>();
      const alerteLigneArret = { id: 29289 };
      jest.spyOn(alerteLigneArretService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alerteLigneArret });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alerteLigneArretService.update).toHaveBeenCalled();
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

    describe('compareAlerteApproche', () => {
      it('should forward to alerteApprocheService', () => {
        const entity = { id: 19175 };
        const entity2 = { id: 16989 };
        jest.spyOn(alerteApprocheService, 'compareAlerteApproche');
        comp.compareAlerteApproche(entity, entity2);
        expect(alerteApprocheService.compareAlerteApproche).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
