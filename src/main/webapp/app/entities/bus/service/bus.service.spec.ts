import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBus } from '../bus.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../bus.test-samples';

import { BusService, RestBus } from './bus.service';

const requireRestSample: RestBus = {
  ...sampleWithRequiredData,
  gpsLastPing: sampleWithRequiredData.gpsLastPing?.toJSON(),
  positionUpdatedAt: sampleWithRequiredData.positionUpdatedAt?.toJSON(),
  dateMiseEnService: sampleWithRequiredData.dateMiseEnService?.format(DATE_FORMAT),
  dateDernierEntretien: sampleWithRequiredData.dateDernierEntretien?.format(DATE_FORMAT),
};

describe('Bus Service', () => {
  let service: BusService;
  let httpMock: HttpTestingController;
  let expectedResult: IBus | IBus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BusService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Bus', () => {
      const bus = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bus', () => {
      const bus = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bus).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Bus', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bus', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Bus', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBusToCollectionIfMissing', () => {
      it('should add a Bus to an empty array', () => {
        const bus: IBus = sampleWithRequiredData;
        expectedResult = service.addBusToCollectionIfMissing([], bus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bus);
      });

      it('should not add a Bus to an array that contains it', () => {
        const bus: IBus = sampleWithRequiredData;
        const busCollection: IBus[] = [
          {
            ...bus,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBusToCollectionIfMissing(busCollection, bus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bus to an array that doesn't contain it", () => {
        const bus: IBus = sampleWithRequiredData;
        const busCollection: IBus[] = [sampleWithPartialData];
        expectedResult = service.addBusToCollectionIfMissing(busCollection, bus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bus);
      });

      it('should add only unique Bus to an array', () => {
        const busArray: IBus[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const busCollection: IBus[] = [sampleWithRequiredData];
        expectedResult = service.addBusToCollectionIfMissing(busCollection, ...busArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bus: IBus = sampleWithRequiredData;
        const bus2: IBus = sampleWithPartialData;
        expectedResult = service.addBusToCollectionIfMissing([], bus, bus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bus);
        expect(expectedResult).toContain(bus2);
      });

      it('should accept null and undefined values', () => {
        const bus: IBus = sampleWithRequiredData;
        expectedResult = service.addBusToCollectionIfMissing([], null, bus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bus);
      });

      it('should return initial array if no Bus is added', () => {
        const busCollection: IBus[] = [sampleWithRequiredData];
        expectedResult = service.addBusToCollectionIfMissing(busCollection, undefined, null);
        expect(expectedResult).toEqual(busCollection);
      });
    });

    describe('compareBus', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBus(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26950 };
        const entity2 = null;

        const compareResult1 = service.compareBus(entity1, entity2);
        const compareResult2 = service.compareBus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26950 };
        const entity2 = { id: 1566 };

        const compareResult1 = service.compareBus(entity1, entity2);
        const compareResult2 = service.compareBus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 26950 };
        const entity2 = { id: 26950 };

        const compareResult1 = service.compareBus(entity1, entity2);
        const compareResult2 = service.compareBus(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
