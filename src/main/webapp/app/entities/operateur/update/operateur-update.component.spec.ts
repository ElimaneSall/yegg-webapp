import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { OperateurService } from '../service/operateur.service';
import { IOperateur } from '../operateur.model';
import { OperateurFormService } from './operateur-form.service';

import { OperateurUpdateComponent } from './operateur-update.component';

describe('Operateur Management Update Component', () => {
  let comp: OperateurUpdateComponent;
  let fixture: ComponentFixture<OperateurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operateurFormService: OperateurFormService;
  let operateurService: OperateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OperateurUpdateComponent],
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
      .overrideTemplate(OperateurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperateurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operateurFormService = TestBed.inject(OperateurFormService);
    operateurService = TestBed.inject(OperateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const operateur: IOperateur = { id: 31481 };

      activatedRoute.data = of({ operateur });
      comp.ngOnInit();

      expect(comp.operateur).toEqual(operateur);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperateur>>();
      const operateur = { id: 26029 };
      jest.spyOn(operateurFormService, 'getOperateur').mockReturnValue(operateur);
      jest.spyOn(operateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operateur }));
      saveSubject.complete();

      // THEN
      expect(operateurFormService.getOperateur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(operateurService.update).toHaveBeenCalledWith(expect.objectContaining(operateur));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperateur>>();
      const operateur = { id: 26029 };
      jest.spyOn(operateurFormService, 'getOperateur').mockReturnValue({ id: null });
      jest.spyOn(operateurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operateur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operateur }));
      saveSubject.complete();

      // THEN
      expect(operateurFormService.getOperateur).toHaveBeenCalled();
      expect(operateurService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperateur>>();
      const operateur = { id: 26029 };
      jest.spyOn(operateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operateurService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
