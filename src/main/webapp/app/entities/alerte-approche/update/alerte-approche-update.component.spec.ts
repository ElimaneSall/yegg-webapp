import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { AlerteApprocheService } from '../service/alerte-approche.service';
import { IAlerteApproche } from '../alerte-approche.model';
import { AlerteApprocheFormService } from './alerte-approche-form.service';

import { AlerteApprocheUpdateComponent } from './alerte-approche-update.component';

describe('AlerteApproche Management Update Component', () => {
  let comp: AlerteApprocheUpdateComponent;
  let fixture: ComponentFixture<AlerteApprocheUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alerteApprocheFormService: AlerteApprocheFormService;
  let alerteApprocheService: AlerteApprocheService;
  let utilisateurService: UtilisateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AlerteApprocheUpdateComponent],
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
      .overrideTemplate(AlerteApprocheUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlerteApprocheUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alerteApprocheFormService = TestBed.inject(AlerteApprocheFormService);
    alerteApprocheService = TestBed.inject(AlerteApprocheService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Utilisateur query and add missing value', () => {
      const alerteApproche: IAlerteApproche = { id: 16989 };
      const utilisateur: IUtilisateur = { id: 2179 };
      alerteApproche.utilisateur = utilisateur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [utilisateur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alerteApproche });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(expect.objectContaining),
      );
      expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const alerteApproche: IAlerteApproche = { id: 16989 };
      const utilisateur: IUtilisateur = { id: 2179 };
      alerteApproche.utilisateur = utilisateur;

      activatedRoute.data = of({ alerteApproche });
      comp.ngOnInit();

      expect(comp.utilisateursSharedCollection).toContainEqual(utilisateur);
      expect(comp.alerteApproche).toEqual(alerteApproche);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlerteApproche>>();
      const alerteApproche = { id: 19175 };
      jest.spyOn(alerteApprocheFormService, 'getAlerteApproche').mockReturnValue(alerteApproche);
      jest.spyOn(alerteApprocheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alerteApproche });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alerteApproche }));
      saveSubject.complete();

      // THEN
      expect(alerteApprocheFormService.getAlerteApproche).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alerteApprocheService.update).toHaveBeenCalledWith(expect.objectContaining(alerteApproche));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlerteApproche>>();
      const alerteApproche = { id: 19175 };
      jest.spyOn(alerteApprocheFormService, 'getAlerteApproche').mockReturnValue({ id: null });
      jest.spyOn(alerteApprocheService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alerteApproche: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alerteApproche }));
      saveSubject.complete();

      // THEN
      expect(alerteApprocheFormService.getAlerteApproche).toHaveBeenCalled();
      expect(alerteApprocheService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlerteApproche>>();
      const alerteApproche = { id: 19175 };
      jest.spyOn(alerteApprocheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alerteApproche });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alerteApprocheService.update).toHaveBeenCalled();
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
