import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ArretService } from '../service/arret.service';
import { IArret } from '../arret.model';
import { ArretFormService } from './arret-form.service';

import { ArretUpdateComponent } from './arret-update.component';

describe('Arret Management Update Component', () => {
  let comp: ArretUpdateComponent;
  let fixture: ComponentFixture<ArretUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let arretFormService: ArretFormService;
  let arretService: ArretService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ArretUpdateComponent],
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
      .overrideTemplate(ArretUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArretUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    arretFormService = TestBed.inject(ArretFormService);
    arretService = TestBed.inject(ArretService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const arret: IArret = { id: 26109 };

      activatedRoute.data = of({ arret });
      comp.ngOnInit();

      expect(comp.arret).toEqual(arret);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArret>>();
      const arret = { id: 23487 };
      jest.spyOn(arretFormService, 'getArret').mockReturnValue(arret);
      jest.spyOn(arretService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arret });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: arret }));
      saveSubject.complete();

      // THEN
      expect(arretFormService.getArret).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(arretService.update).toHaveBeenCalledWith(expect.objectContaining(arret));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArret>>();
      const arret = { id: 23487 };
      jest.spyOn(arretFormService, 'getArret').mockReturnValue({ id: null });
      jest.spyOn(arretService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arret: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: arret }));
      saveSubject.complete();

      // THEN
      expect(arretFormService.getArret).toHaveBeenCalled();
      expect(arretService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArret>>();
      const arret = { id: 23487 };
      jest.spyOn(arretService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arret });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(arretService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
