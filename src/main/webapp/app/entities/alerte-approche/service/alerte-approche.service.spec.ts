import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAlerteApproche } from '../alerte-approche.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../alerte-approche.test-samples';

import { AlerteApprocheService, RestAlerteApproche } from './alerte-approche.service';

const requireRestSample: RestAlerteApproche = {
  ...sampleWithRequiredData,
  dateCreation: sampleWithRequiredData.dateCreation?.toJSON(),
  dateModification: sampleWithRequiredData.dateModification?.toJSON(),
  dernierDeclenchement: sampleWithRequiredData.dernierDeclenchement?.toJSON(),
};

describe('AlerteApproche Service', () => {
  let service: AlerteApprocheService;
  let httpMock: HttpTestingController;
  let expectedResult: IAlerteApproche | IAlerteApproche[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AlerteApprocheService);
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

    it('should create a AlerteApproche', () => {
      const alerteApproche = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(alerteApproche).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AlerteApproche', () => {
      const alerteApproche = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(alerteApproche).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AlerteApproche', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AlerteApproche', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AlerteApproche', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAlerteApprocheToCollectionIfMissing', () => {
      it('should add a AlerteApproche to an empty array', () => {
        const alerteApproche: IAlerteApproche = sampleWithRequiredData;
        expectedResult = service.addAlerteApprocheToCollectionIfMissing([], alerteApproche);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alerteApproche);
      });

      it('should not add a AlerteApproche to an array that contains it', () => {
        const alerteApproche: IAlerteApproche = sampleWithRequiredData;
        const alerteApprocheCollection: IAlerteApproche[] = [
          {
            ...alerteApproche,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAlerteApprocheToCollectionIfMissing(alerteApprocheCollection, alerteApproche);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AlerteApproche to an array that doesn't contain it", () => {
        const alerteApproche: IAlerteApproche = sampleWithRequiredData;
        const alerteApprocheCollection: IAlerteApproche[] = [sampleWithPartialData];
        expectedResult = service.addAlerteApprocheToCollectionIfMissing(alerteApprocheCollection, alerteApproche);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alerteApproche);
      });

      it('should add only unique AlerteApproche to an array', () => {
        const alerteApprocheArray: IAlerteApproche[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const alerteApprocheCollection: IAlerteApproche[] = [sampleWithRequiredData];
        expectedResult = service.addAlerteApprocheToCollectionIfMissing(alerteApprocheCollection, ...alerteApprocheArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const alerteApproche: IAlerteApproche = sampleWithRequiredData;
        const alerteApproche2: IAlerteApproche = sampleWithPartialData;
        expectedResult = service.addAlerteApprocheToCollectionIfMissing([], alerteApproche, alerteApproche2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alerteApproche);
        expect(expectedResult).toContain(alerteApproche2);
      });

      it('should accept null and undefined values', () => {
        const alerteApproche: IAlerteApproche = sampleWithRequiredData;
        expectedResult = service.addAlerteApprocheToCollectionIfMissing([], null, alerteApproche, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alerteApproche);
      });

      it('should return initial array if no AlerteApproche is added', () => {
        const alerteApprocheCollection: IAlerteApproche[] = [sampleWithRequiredData];
        expectedResult = service.addAlerteApprocheToCollectionIfMissing(alerteApprocheCollection, undefined, null);
        expect(expectedResult).toEqual(alerteApprocheCollection);
      });
    });

    describe('compareAlerteApproche', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAlerteApproche(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19175 };
        const entity2 = null;

        const compareResult1 = service.compareAlerteApproche(entity1, entity2);
        const compareResult2 = service.compareAlerteApproche(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19175 };
        const entity2 = { id: 16989 };

        const compareResult1 = service.compareAlerteApproche(entity1, entity2);
        const compareResult2 = service.compareAlerteApproche(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 19175 };
        const entity2 = { id: 19175 };

        const compareResult1 = service.compareAlerteApproche(entity1, entity2);
        const compareResult2 = service.compareAlerteApproche(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
