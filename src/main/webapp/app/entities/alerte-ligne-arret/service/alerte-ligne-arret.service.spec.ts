import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAlerteLigneArret } from '../alerte-ligne-arret.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../alerte-ligne-arret.test-samples';

import { AlerteLigneArretService } from './alerte-ligne-arret.service';

const requireRestSample: IAlerteLigneArret = {
  ...sampleWithRequiredData,
};

describe('AlerteLigneArret Service', () => {
  let service: AlerteLigneArretService;
  let httpMock: HttpTestingController;
  let expectedResult: IAlerteLigneArret | IAlerteLigneArret[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AlerteLigneArretService);
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

    it('should create a AlerteLigneArret', () => {
      const alerteLigneArret = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(alerteLigneArret).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AlerteLigneArret', () => {
      const alerteLigneArret = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(alerteLigneArret).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AlerteLigneArret', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AlerteLigneArret', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AlerteLigneArret', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAlerteLigneArretToCollectionIfMissing', () => {
      it('should add a AlerteLigneArret to an empty array', () => {
        const alerteLigneArret: IAlerteLigneArret = sampleWithRequiredData;
        expectedResult = service.addAlerteLigneArretToCollectionIfMissing([], alerteLigneArret);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alerteLigneArret);
      });

      it('should not add a AlerteLigneArret to an array that contains it', () => {
        const alerteLigneArret: IAlerteLigneArret = sampleWithRequiredData;
        const alerteLigneArretCollection: IAlerteLigneArret[] = [
          {
            ...alerteLigneArret,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAlerteLigneArretToCollectionIfMissing(alerteLigneArretCollection, alerteLigneArret);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AlerteLigneArret to an array that doesn't contain it", () => {
        const alerteLigneArret: IAlerteLigneArret = sampleWithRequiredData;
        const alerteLigneArretCollection: IAlerteLigneArret[] = [sampleWithPartialData];
        expectedResult = service.addAlerteLigneArretToCollectionIfMissing(alerteLigneArretCollection, alerteLigneArret);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alerteLigneArret);
      });

      it('should add only unique AlerteLigneArret to an array', () => {
        const alerteLigneArretArray: IAlerteLigneArret[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const alerteLigneArretCollection: IAlerteLigneArret[] = [sampleWithRequiredData];
        expectedResult = service.addAlerteLigneArretToCollectionIfMissing(alerteLigneArretCollection, ...alerteLigneArretArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const alerteLigneArret: IAlerteLigneArret = sampleWithRequiredData;
        const alerteLigneArret2: IAlerteLigneArret = sampleWithPartialData;
        expectedResult = service.addAlerteLigneArretToCollectionIfMissing([], alerteLigneArret, alerteLigneArret2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alerteLigneArret);
        expect(expectedResult).toContain(alerteLigneArret2);
      });

      it('should accept null and undefined values', () => {
        const alerteLigneArret: IAlerteLigneArret = sampleWithRequiredData;
        expectedResult = service.addAlerteLigneArretToCollectionIfMissing([], null, alerteLigneArret, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alerteLigneArret);
      });

      it('should return initial array if no AlerteLigneArret is added', () => {
        const alerteLigneArretCollection: IAlerteLigneArret[] = [sampleWithRequiredData];
        expectedResult = service.addAlerteLigneArretToCollectionIfMissing(alerteLigneArretCollection, undefined, null);
        expect(expectedResult).toEqual(alerteLigneArretCollection);
      });
    });

    describe('compareAlerteLigneArret', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAlerteLigneArret(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29289 };
        const entity2 = null;

        const compareResult1 = service.compareAlerteLigneArret(entity1, entity2);
        const compareResult2 = service.compareAlerteLigneArret(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29289 };
        const entity2 = { id: 11413 };

        const compareResult1 = service.compareAlerteLigneArret(entity1, entity2);
        const compareResult2 = service.compareAlerteLigneArret(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 29289 };
        const entity2 = { id: 29289 };

        const compareResult1 = service.compareAlerteLigneArret(entity1, entity2);
        const compareResult2 = service.compareAlerteLigneArret(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
