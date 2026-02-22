import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IBus } from 'app/entities/bus/bus.model';
import { BusService } from 'app/entities/bus/service/bus.service';
import { TrackingService } from '../service/tracking.service';
import { ITracking } from '../tracking.model';
import { TrackingFormService } from './tracking-form.service';

import { TrackingUpdateComponent } from './tracking-update.component';

describe('Tracking Management Update Component', () => {
  let comp: TrackingUpdateComponent;
  let fixture: ComponentFixture<TrackingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trackingFormService: TrackingFormService;
  let trackingService: TrackingService;
  let busService: BusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrackingUpdateComponent],
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
      .overrideTemplate(TrackingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrackingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trackingFormService = TestBed.inject(TrackingFormService);
    trackingService = TestBed.inject(TrackingService);
    busService = TestBed.inject(BusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Bus query and add missing value', () => {
      const tracking: ITracking = { id: 13594 };
      const bus: IBus = { id: 26950 };
      tracking.bus = bus;

      const busCollection: IBus[] = [{ id: 26950 }];
      jest.spyOn(busService, 'query').mockReturnValue(of(new HttpResponse({ body: busCollection })));
      const additionalBuses = [bus];
      const expectedCollection: IBus[] = [...additionalBuses, ...busCollection];
      jest.spyOn(busService, 'addBusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tracking });
      comp.ngOnInit();

      expect(busService.query).toHaveBeenCalled();
      expect(busService.addBusToCollectionIfMissing).toHaveBeenCalledWith(busCollection, ...additionalBuses.map(expect.objectContaining));
      expect(comp.busesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const tracking: ITracking = { id: 13594 };
      const bus: IBus = { id: 26950 };
      tracking.bus = bus;

      activatedRoute.data = of({ tracking });
      comp.ngOnInit();

      expect(comp.busesSharedCollection).toContainEqual(bus);
      expect(comp.tracking).toEqual(tracking);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITracking>>();
      const tracking = { id: 1622 };
      jest.spyOn(trackingFormService, 'getTracking').mockReturnValue(tracking);
      jest.spyOn(trackingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tracking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tracking }));
      saveSubject.complete();

      // THEN
      expect(trackingFormService.getTracking).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trackingService.update).toHaveBeenCalledWith(expect.objectContaining(tracking));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITracking>>();
      const tracking = { id: 1622 };
      jest.spyOn(trackingFormService, 'getTracking').mockReturnValue({ id: null });
      jest.spyOn(trackingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tracking: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tracking }));
      saveSubject.complete();

      // THEN
      expect(trackingFormService.getTracking).toHaveBeenCalled();
      expect(trackingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITracking>>();
      const tracking = { id: 1622 };
      jest.spyOn(trackingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tracking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trackingService.update).toHaveBeenCalled();
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
  });
});
