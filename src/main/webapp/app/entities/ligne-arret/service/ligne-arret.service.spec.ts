import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ILigneArret } from '../ligne-arret.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ligne-arret.test-samples';

import { LigneArretService } from './ligne-arret.service';

const requireRestSample: ILigneArret = {
  ...sampleWithRequiredData,
};

describe('LigneArret Service', () => {
  let service: LigneArretService;
  let httpMock: HttpTestingController;
  let expectedResult: ILigneArret | ILigneArret[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LigneArretService);
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

    it('should create a LigneArret', () => {
      const ligneArret = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ligneArret).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LigneArret', () => {
      const ligneArret = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ligneArret).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LigneArret', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LigneArret', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LigneArret', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLigneArretToCollectionIfMissing', () => {
      it('should add a LigneArret to an empty array', () => {
        const ligneArret: ILigneArret = sampleWithRequiredData;
        expectedResult = service.addLigneArretToCollectionIfMissing([], ligneArret);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ligneArret);
      });

      it('should not add a LigneArret to an array that contains it', () => {
        const ligneArret: ILigneArret = sampleWithRequiredData;
        const ligneArretCollection: ILigneArret[] = [
          {
            ...ligneArret,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLigneArretToCollectionIfMissing(ligneArretCollection, ligneArret);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LigneArret to an array that doesn't contain it", () => {
        const ligneArret: ILigneArret = sampleWithRequiredData;
        const ligneArretCollection: ILigneArret[] = [sampleWithPartialData];
        expectedResult = service.addLigneArretToCollectionIfMissing(ligneArretCollection, ligneArret);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ligneArret);
      });

      it('should add only unique LigneArret to an array', () => {
        const ligneArretArray: ILigneArret[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ligneArretCollection: ILigneArret[] = [sampleWithRequiredData];
        expectedResult = service.addLigneArretToCollectionIfMissing(ligneArretCollection, ...ligneArretArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ligneArret: ILigneArret = sampleWithRequiredData;
        const ligneArret2: ILigneArret = sampleWithPartialData;
        expectedResult = service.addLigneArretToCollectionIfMissing([], ligneArret, ligneArret2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ligneArret);
        expect(expectedResult).toContain(ligneArret2);
      });

      it('should accept null and undefined values', () => {
        const ligneArret: ILigneArret = sampleWithRequiredData;
        expectedResult = service.addLigneArretToCollectionIfMissing([], null, ligneArret, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ligneArret);
      });

      it('should return initial array if no LigneArret is added', () => {
        const ligneArretCollection: ILigneArret[] = [sampleWithRequiredData];
        expectedResult = service.addLigneArretToCollectionIfMissing(ligneArretCollection, undefined, null);
        expect(expectedResult).toEqual(ligneArretCollection);
      });
    });

    describe('compareLigneArret', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLigneArret(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 24214 };
        const entity2 = null;

        const compareResult1 = service.compareLigneArret(entity1, entity2);
        const compareResult2 = service.compareLigneArret(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 24214 };
        const entity2 = { id: 30257 };

        const compareResult1 = service.compareLigneArret(entity1, entity2);
        const compareResult2 = service.compareLigneArret(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 24214 };
        const entity2 = { id: 24214 };

        const compareResult1 = service.compareLigneArret(entity1, entity2);
        const compareResult2 = service.compareLigneArret(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
