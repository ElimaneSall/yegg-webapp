import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IBus } from 'app/entities/bus/bus.model';
import { BusService } from 'app/entities/bus/service/bus.service';
import { IAlerteApproche } from 'app/entities/alerte-approche/alerte-approche.model';
import { AlerteApprocheService } from 'app/entities/alerte-approche/service/alerte-approche.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IHistoriqueAlerte } from '../historique-alerte.model';
import { HistoriqueAlerteService } from '../service/historique-alerte.service';
import { HistoriqueAlerteFormService } from './historique-alerte-form.service';

import { HistoriqueAlerteUpdateComponent } from './historique-alerte-update.component';

describe('HistoriqueAlerte Management Update Component', () => {
  let comp: HistoriqueAlerteUpdateComponent;
  let fixture: ComponentFixture<HistoriqueAlerteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let historiqueAlerteFormService: HistoriqueAlerteFormService;
  let historiqueAlerteService: HistoriqueAlerteService;
  let busService: BusService;
  let alerteApprocheService: AlerteApprocheService;
  let utilisateurService: UtilisateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HistoriqueAlerteUpdateComponent],
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
      .overrideTemplate(HistoriqueAlerteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HistoriqueAlerteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    historiqueAlerteFormService = TestBed.inject(HistoriqueAlerteFormService);
    historiqueAlerteService = TestBed.inject(HistoriqueAlerteService);
    busService = TestBed.inject(BusService);
    alerteApprocheService = TestBed.inject(AlerteApprocheService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Bus query and add missing value', () => {
      const historiqueAlerte: IHistoriqueAlerte = { id: 14561 };
      const bus: IBus = { id: 26950 };
      historiqueAlerte.bus = bus;

      const busCollection: IBus[] = [{ id: 26950 }];
      jest.spyOn(busService, 'query').mockReturnValue(of(new HttpResponse({ body: busCollection })));
      const additionalBuses = [bus];
      const expectedCollection: IBus[] = [...additionalBuses, ...busCollection];
      jest.spyOn(busService, 'addBusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historiqueAlerte });
      comp.ngOnInit();

      expect(busService.query).toHaveBeenCalled();
      expect(busService.addBusToCollectionIfMissing).toHaveBeenCalledWith(busCollection, ...additionalBuses.map(expect.objectContaining));
      expect(comp.busesSharedCollection).toEqual(expectedCollection);
    });

    it('should call AlerteApproche query and add missing value', () => {
      const historiqueAlerte: IHistoriqueAlerte = { id: 14561 };
      const alerteApproche: IAlerteApproche = { id: 19175 };
      historiqueAlerte.alerteApproche = alerteApproche;

      const alerteApprocheCollection: IAlerteApproche[] = [{ id: 19175 }];
      jest.spyOn(alerteApprocheService, 'query').mockReturnValue(of(new HttpResponse({ body: alerteApprocheCollection })));
      const additionalAlerteApproches = [alerteApproche];
      const expectedCollection: IAlerteApproche[] = [...additionalAlerteApproches, ...alerteApprocheCollection];
      jest.spyOn(alerteApprocheService, 'addAlerteApprocheToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historiqueAlerte });
      comp.ngOnInit();

      expect(alerteApprocheService.query).toHaveBeenCalled();
      expect(alerteApprocheService.addAlerteApprocheToCollectionIfMissing).toHaveBeenCalledWith(
        alerteApprocheCollection,
        ...additionalAlerteApproches.map(expect.objectContaining),
      );
      expect(comp.alerteApprochesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Utilisateur query and add missing value', () => {
      const historiqueAlerte: IHistoriqueAlerte = { id: 14561 };
      const utilisateur: IUtilisateur = { id: 2179 };
      historiqueAlerte.utilisateur = utilisateur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [utilisateur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historiqueAlerte });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(expect.objectContaining),
      );
      expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const historiqueAlerte: IHistoriqueAlerte = { id: 14561 };
      const bus: IBus = { id: 26950 };
      historiqueAlerte.bus = bus;
      const alerteApproche: IAlerteApproche = { id: 19175 };
      historiqueAlerte.alerteApproche = alerteApproche;
      const utilisateur: IUtilisateur = { id: 2179 };
      historiqueAlerte.utilisateur = utilisateur;

      activatedRoute.data = of({ historiqueAlerte });
      comp.ngOnInit();

      expect(comp.busesSharedCollection).toContainEqual(bus);
      expect(comp.alerteApprochesSharedCollection).toContainEqual(alerteApproche);
      expect(comp.utilisateursSharedCollection).toContainEqual(utilisateur);
      expect(comp.historiqueAlerte).toEqual(historiqueAlerte);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoriqueAlerte>>();
      const historiqueAlerte = { id: 4252 };
      jest.spyOn(historiqueAlerteFormService, 'getHistoriqueAlerte').mockReturnValue(historiqueAlerte);
      jest.spyOn(historiqueAlerteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historiqueAlerte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historiqueAlerte }));
      saveSubject.complete();

      // THEN
      expect(historiqueAlerteFormService.getHistoriqueAlerte).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(historiqueAlerteService.update).toHaveBeenCalledWith(expect.objectContaining(historiqueAlerte));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoriqueAlerte>>();
      const historiqueAlerte = { id: 4252 };
      jest.spyOn(historiqueAlerteFormService, 'getHistoriqueAlerte').mockReturnValue({ id: null });
      jest.spyOn(historiqueAlerteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historiqueAlerte: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historiqueAlerte }));
      saveSubject.complete();

      // THEN
      expect(historiqueAlerteFormService.getHistoriqueAlerte).toHaveBeenCalled();
      expect(historiqueAlerteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoriqueAlerte>>();
      const historiqueAlerte = { id: 4252 };
      jest.spyOn(historiqueAlerteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historiqueAlerte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(historiqueAlerteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBus', () => {
      it('should forward to busService', () => {
        const entity = { id: 26950 };
        const entity2 = { id: 1566 };
        jest.spyOn(busService, 'compareBus');
        comp.compareBus(entity, entity2);
        expect(busService.compareBus).toHaveBeenCalledWith(entity, entity2);
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
