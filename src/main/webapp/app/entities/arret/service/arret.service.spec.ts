import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IArret } from '../arret.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../arret.test-samples';

import { ArretService } from './arret.service';

const requireRestSample: IArret = {
  ...sampleWithRequiredData,
};

describe('Arret Service', () => {
  let service: ArretService;
  let httpMock: HttpTestingController;
  let expectedResult: IArret | IArret[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ArretService);
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

    it('should create a Arret', () => {
      const arret = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(arret).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Arret', () => {
      const arret = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(arret).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Arret', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Arret', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Arret', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addArretToCollectionIfMissing', () => {
      it('should add a Arret to an empty array', () => {
        const arret: IArret = sampleWithRequiredData;
        expectedResult = service.addArretToCollectionIfMissing([], arret);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(arret);
      });

      it('should not add a Arret to an array that contains it', () => {
        const arret: IArret = sampleWithRequiredData;
        const arretCollection: IArret[] = [
          {
            ...arret,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addArretToCollectionIfMissing(arretCollection, arret);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Arret to an array that doesn't contain it", () => {
        const arret: IArret = sampleWithRequiredData;
        const arretCollection: IArret[] = [sampleWithPartialData];
        expectedResult = service.addArretToCollectionIfMissing(arretCollection, arret);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(arret);
      });

      it('should add only unique Arret to an array', () => {
        const arretArray: IArret[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const arretCollection: IArret[] = [sampleWithRequiredData];
        expectedResult = service.addArretToCollectionIfMissing(arretCollection, ...arretArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const arret: IArret = sampleWithRequiredData;
        const arret2: IArret = sampleWithPartialData;
        expectedResult = service.addArretToCollectionIfMissing([], arret, arret2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(arret);
        expect(expectedResult).toContain(arret2);
      });

      it('should accept null and undefined values', () => {
        const arret: IArret = sampleWithRequiredData;
        expectedResult = service.addArretToCollectionIfMissing([], null, arret, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(arret);
      });

      it('should return initial array if no Arret is added', () => {
        const arretCollection: IArret[] = [sampleWithRequiredData];
        expectedResult = service.addArretToCollectionIfMissing(arretCollection, undefined, null);
        expect(expectedResult).toEqual(arretCollection);
      });
    });

    describe('compareArret', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareArret(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23487 };
        const entity2 = null;

        const compareResult1 = service.compareArret(entity1, entity2);
        const compareResult2 = service.compareArret(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23487 };
        const entity2 = { id: 26109 };

        const compareResult1 = service.compareArret(entity1, entity2);
        const compareResult2 = service.compareArret(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 23487 };
        const entity2 = { id: 23487 };

        const compareResult1 = service.compareArret(entity1, entity2);
        const compareResult2 = service.compareArret(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
