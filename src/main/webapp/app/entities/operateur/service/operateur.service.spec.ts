import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOperateur } from '../operateur.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../operateur.test-samples';

import { OperateurService } from './operateur.service';

const requireRestSample: IOperateur = {
  ...sampleWithRequiredData,
};

describe('Operateur Service', () => {
  let service: OperateurService;
  let httpMock: HttpTestingController;
  let expectedResult: IOperateur | IOperateur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OperateurService);
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

    it('should create a Operateur', () => {
      const operateur = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(operateur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Operateur', () => {
      const operateur = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(operateur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Operateur', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Operateur', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Operateur', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOperateurToCollectionIfMissing', () => {
      it('should add a Operateur to an empty array', () => {
        const operateur: IOperateur = sampleWithRequiredData;
        expectedResult = service.addOperateurToCollectionIfMissing([], operateur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operateur);
      });

      it('should not add a Operateur to an array that contains it', () => {
        const operateur: IOperateur = sampleWithRequiredData;
        const operateurCollection: IOperateur[] = [
          {
            ...operateur,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOperateurToCollectionIfMissing(operateurCollection, operateur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Operateur to an array that doesn't contain it", () => {
        const operateur: IOperateur = sampleWithRequiredData;
        const operateurCollection: IOperateur[] = [sampleWithPartialData];
        expectedResult = service.addOperateurToCollectionIfMissing(operateurCollection, operateur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operateur);
      });

      it('should add only unique Operateur to an array', () => {
        const operateurArray: IOperateur[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const operateurCollection: IOperateur[] = [sampleWithRequiredData];
        expectedResult = service.addOperateurToCollectionIfMissing(operateurCollection, ...operateurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const operateur: IOperateur = sampleWithRequiredData;
        const operateur2: IOperateur = sampleWithPartialData;
        expectedResult = service.addOperateurToCollectionIfMissing([], operateur, operateur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operateur);
        expect(expectedResult).toContain(operateur2);
      });

      it('should accept null and undefined values', () => {
        const operateur: IOperateur = sampleWithRequiredData;
        expectedResult = service.addOperateurToCollectionIfMissing([], null, operateur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operateur);
      });

      it('should return initial array if no Operateur is added', () => {
        const operateurCollection: IOperateur[] = [sampleWithRequiredData];
        expectedResult = service.addOperateurToCollectionIfMissing(operateurCollection, undefined, null);
        expect(expectedResult).toEqual(operateurCollection);
      });
    });

    describe('compareOperateur', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOperateur(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26029 };
        const entity2 = null;

        const compareResult1 = service.compareOperateur(entity1, entity2);
        const compareResult2 = service.compareOperateur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26029 };
        const entity2 = { id: 31481 };

        const compareResult1 = service.compareOperateur(entity1, entity2);
        const compareResult2 = service.compareOperateur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 26029 };
        const entity2 = { id: 26029 };

        const compareResult1 = service.compareOperateur(entity1, entity2);
        const compareResult2 = service.compareOperateur(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
