import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFavori } from '../favori.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../favori.test-samples';

import { FavoriService, RestFavori } from './favori.service';

const requireRestSample: RestFavori = {
  ...sampleWithRequiredData,
  dateAjout: sampleWithRequiredData.dateAjout?.toJSON(),
  dernierAcces: sampleWithRequiredData.dernierAcces?.toJSON(),
};

describe('Favori Service', () => {
  let service: FavoriService;
  let httpMock: HttpTestingController;
  let expectedResult: IFavori | IFavori[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FavoriService);
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

    it('should create a Favori', () => {
      const favori = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(favori).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Favori', () => {
      const favori = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(favori).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Favori', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Favori', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Favori', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFavoriToCollectionIfMissing', () => {
      it('should add a Favori to an empty array', () => {
        const favori: IFavori = sampleWithRequiredData;
        expectedResult = service.addFavoriToCollectionIfMissing([], favori);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(favori);
      });

      it('should not add a Favori to an array that contains it', () => {
        const favori: IFavori = sampleWithRequiredData;
        const favoriCollection: IFavori[] = [
          {
            ...favori,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFavoriToCollectionIfMissing(favoriCollection, favori);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Favori to an array that doesn't contain it", () => {
        const favori: IFavori = sampleWithRequiredData;
        const favoriCollection: IFavori[] = [sampleWithPartialData];
        expectedResult = service.addFavoriToCollectionIfMissing(favoriCollection, favori);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(favori);
      });

      it('should add only unique Favori to an array', () => {
        const favoriArray: IFavori[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const favoriCollection: IFavori[] = [sampleWithRequiredData];
        expectedResult = service.addFavoriToCollectionIfMissing(favoriCollection, ...favoriArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const favori: IFavori = sampleWithRequiredData;
        const favori2: IFavori = sampleWithPartialData;
        expectedResult = service.addFavoriToCollectionIfMissing([], favori, favori2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(favori);
        expect(expectedResult).toContain(favori2);
      });

      it('should accept null and undefined values', () => {
        const favori: IFavori = sampleWithRequiredData;
        expectedResult = service.addFavoriToCollectionIfMissing([], null, favori, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(favori);
      });

      it('should return initial array if no Favori is added', () => {
        const favoriCollection: IFavori[] = [sampleWithRequiredData];
        expectedResult = service.addFavoriToCollectionIfMissing(favoriCollection, undefined, null);
        expect(expectedResult).toEqual(favoriCollection);
      });
    });

    describe('compareFavori', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFavori(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 20334 };
        const entity2 = null;

        const compareResult1 = service.compareFavori(entity1, entity2);
        const compareResult2 = service.compareFavori(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 20334 };
        const entity2 = { id: 28168 };

        const compareResult1 = service.compareFavori(entity1, entity2);
        const compareResult2 = service.compareFavori(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 20334 };
        const entity2 = { id: 20334 };

        const compareResult1 = service.compareFavori(entity1, entity2);
        const compareResult2 = service.compareFavori(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
