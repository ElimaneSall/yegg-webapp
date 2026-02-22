import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ILigne } from '../ligne.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ligne.test-samples';

import { LigneService } from './ligne.service';

const requireRestSample: ILigne = {
  ...sampleWithRequiredData,
};

describe('Ligne Service', () => {
  let service: LigneService;
  let httpMock: HttpTestingController;
  let expectedResult: ILigne | ILigne[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LigneService);
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

    it('should create a Ligne', () => {
      const ligne = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ligne).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ligne', () => {
      const ligne = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ligne).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ligne', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ligne', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ligne', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLigneToCollectionIfMissing', () => {
      it('should add a Ligne to an empty array', () => {
        const ligne: ILigne = sampleWithRequiredData;
        expectedResult = service.addLigneToCollectionIfMissing([], ligne);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ligne);
      });

      it('should not add a Ligne to an array that contains it', () => {
        const ligne: ILigne = sampleWithRequiredData;
        const ligneCollection: ILigne[] = [
          {
            ...ligne,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLigneToCollectionIfMissing(ligneCollection, ligne);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ligne to an array that doesn't contain it", () => {
        const ligne: ILigne = sampleWithRequiredData;
        const ligneCollection: ILigne[] = [sampleWithPartialData];
        expectedResult = service.addLigneToCollectionIfMissing(ligneCollection, ligne);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ligne);
      });

      it('should add only unique Ligne to an array', () => {
        const ligneArray: ILigne[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ligneCollection: ILigne[] = [sampleWithRequiredData];
        expectedResult = service.addLigneToCollectionIfMissing(ligneCollection, ...ligneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ligne: ILigne = sampleWithRequiredData;
        const ligne2: ILigne = sampleWithPartialData;
        expectedResult = service.addLigneToCollectionIfMissing([], ligne, ligne2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ligne);
        expect(expectedResult).toContain(ligne2);
      });

      it('should accept null and undefined values', () => {
        const ligne: ILigne = sampleWithRequiredData;
        expectedResult = service.addLigneToCollectionIfMissing([], null, ligne, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ligne);
      });

      it('should return initial array if no Ligne is added', () => {
        const ligneCollection: ILigne[] = [sampleWithRequiredData];
        expectedResult = service.addLigneToCollectionIfMissing(ligneCollection, undefined, null);
        expect(expectedResult).toEqual(ligneCollection);
      });
    });

    describe('compareLigne', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLigne(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18806 };
        const entity2 = null;

        const compareResult1 = service.compareLigne(entity1, entity2);
        const compareResult2 = service.compareLigne(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18806 };
        const entity2 = { id: 21722 };

        const compareResult1 = service.compareLigne(entity1, entity2);
        const compareResult2 = service.compareLigne(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 18806 };
        const entity2 = { id: 18806 };

        const compareResult1 = service.compareLigne(entity1, entity2);
        const compareResult2 = service.compareLigne(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
