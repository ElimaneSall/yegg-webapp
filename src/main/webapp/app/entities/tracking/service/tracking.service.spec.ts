import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITracking } from '../tracking.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../tracking.test-samples';

import { RestTracking, TrackingService } from './tracking.service';

const requireRestSample: RestTracking = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('Tracking Service', () => {
  let service: TrackingService;
  let httpMock: HttpTestingController;
  let expectedResult: ITracking | ITracking[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TrackingService);
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

    it('should create a Tracking', () => {
      const tracking = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tracking).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tracking', () => {
      const tracking = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tracking).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tracking', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tracking', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Tracking', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrackingToCollectionIfMissing', () => {
      it('should add a Tracking to an empty array', () => {
        const tracking: ITracking = sampleWithRequiredData;
        expectedResult = service.addTrackingToCollectionIfMissing([], tracking);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tracking);
      });

      it('should not add a Tracking to an array that contains it', () => {
        const tracking: ITracking = sampleWithRequiredData;
        const trackingCollection: ITracking[] = [
          {
            ...tracking,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrackingToCollectionIfMissing(trackingCollection, tracking);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tracking to an array that doesn't contain it", () => {
        const tracking: ITracking = sampleWithRequiredData;
        const trackingCollection: ITracking[] = [sampleWithPartialData];
        expectedResult = service.addTrackingToCollectionIfMissing(trackingCollection, tracking);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tracking);
      });

      it('should add only unique Tracking to an array', () => {
        const trackingArray: ITracking[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trackingCollection: ITracking[] = [sampleWithRequiredData];
        expectedResult = service.addTrackingToCollectionIfMissing(trackingCollection, ...trackingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tracking: ITracking = sampleWithRequiredData;
        const tracking2: ITracking = sampleWithPartialData;
        expectedResult = service.addTrackingToCollectionIfMissing([], tracking, tracking2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tracking);
        expect(expectedResult).toContain(tracking2);
      });

      it('should accept null and undefined values', () => {
        const tracking: ITracking = sampleWithRequiredData;
        expectedResult = service.addTrackingToCollectionIfMissing([], null, tracking, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tracking);
      });

      it('should return initial array if no Tracking is added', () => {
        const trackingCollection: ITracking[] = [sampleWithRequiredData];
        expectedResult = service.addTrackingToCollectionIfMissing(trackingCollection, undefined, null);
        expect(expectedResult).toEqual(trackingCollection);
      });
    });

    describe('compareTracking', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTracking(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1622 };
        const entity2 = null;

        const compareResult1 = service.compareTracking(entity1, entity2);
        const compareResult2 = service.compareTracking(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1622 };
        const entity2 = { id: 13594 };

        const compareResult1 = service.compareTracking(entity1, entity2);
        const compareResult2 = service.compareTracking(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 1622 };
        const entity2 = { id: 1622 };

        const compareResult1 = service.compareTracking(entity1, entity2);
        const compareResult2 = service.compareTracking(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
