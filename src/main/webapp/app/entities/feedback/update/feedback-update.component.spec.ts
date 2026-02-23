import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { FeedbackService } from '../service/feedback.service';
import { IFeedback } from '../feedback.model';
import { FeedbackFormService } from './feedback-form.service';

import { FeedbackUpdateComponent } from './feedback-update.component';

describe('Feedback Management Update Component', () => {
  let comp: FeedbackUpdateComponent;
  let fixture: ComponentFixture<FeedbackUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackFormService: FeedbackFormService;
  let feedbackService: FeedbackService;
  let utilisateurService: UtilisateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FeedbackUpdateComponent],
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
      .overrideTemplate(FeedbackUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackFormService = TestBed.inject(FeedbackFormService);
    feedbackService = TestBed.inject(FeedbackService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Utilisateur query and add missing value', () => {
      const feedback: IFeedback = { id: 1452 };
      const utilisateur: IUtilisateur = { id: 2179 };
      feedback.utilisateur = utilisateur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [utilisateur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(expect.objectContaining),
      );
      expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const feedback: IFeedback = { id: 1452 };
      const utilisateur: IUtilisateur = { id: 2179 };
      feedback.utilisateur = utilisateur;

      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      expect(comp.utilisateursSharedCollection).toContainEqual(utilisateur);
      expect(comp.feedback).toEqual(feedback);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedback>>();
      const feedback = { id: 10592 };
      jest.spyOn(feedbackFormService, 'getFeedback').mockReturnValue(feedback);
      jest.spyOn(feedbackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedback }));
      saveSubject.complete();

      // THEN
      expect(feedbackFormService.getFeedback).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackService.update).toHaveBeenCalledWith(expect.objectContaining(feedback));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedback>>();
      const feedback = { id: 10592 };
      jest.spyOn(feedbackFormService, 'getFeedback').mockReturnValue({ id: null });
      jest.spyOn(feedbackService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedback: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedback }));
      saveSubject.complete();

      // THEN
      expect(feedbackFormService.getFeedback).toHaveBeenCalled();
      expect(feedbackService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedback>>();
      const feedback = { id: 10592 };
      jest.spyOn(feedbackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackService.update).toHaveBeenCalled();
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
