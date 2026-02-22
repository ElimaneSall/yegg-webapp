import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAlerte } from '../alerte.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../alerte.test-samples';

import { AlerteService, RestAlerte } from './alerte.service';

const requireRestSample: RestAlerte = {
  ...sampleWithRequiredData,
  heureDebut: sampleWithRequiredData.heureDebut?.toJSON(),
  heureFin: sampleWithRequiredData.heureFin?.toJSON(),
  dernierDeclenchement: sampleWithRequiredData.dernierDeclenchement?.toJSON(),
};

describe('Alerte Service', () => {
  let service: AlerteService;
  let httpMock: HttpTestingController;
  let expectedResult: IAlerte | IAlerte[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AlerteService);
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

    it('should create a Alerte', () => {
      const alerte = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(alerte).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Alerte', () => {
      const alerte = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(alerte).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Alerte', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Alerte', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Alerte', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAlerteToCollectionIfMissing', () => {
      it('should add a Alerte to an empty array', () => {
        const alerte: IAlerte = sampleWithRequiredData;
        expectedResult = service.addAlerteToCollectionIfMissing([], alerte);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alerte);
      });

      it('should not add a Alerte to an array that contains it', () => {
        const alerte: IAlerte = sampleWithRequiredData;
        const alerteCollection: IAlerte[] = [
          {
            ...alerte,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAlerteToCollectionIfMissing(alerteCollection, alerte);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Alerte to an array that doesn't contain it", () => {
        const alerte: IAlerte = sampleWithRequiredData;
        const alerteCollection: IAlerte[] = [sampleWithPartialData];
        expectedResult = service.addAlerteToCollectionIfMissing(alerteCollection, alerte);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alerte);
      });

      it('should add only unique Alerte to an array', () => {
        const alerteArray: IAlerte[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const alerteCollection: IAlerte[] = [sampleWithRequiredData];
        expectedResult = service.addAlerteToCollectionIfMissing(alerteCollection, ...alerteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const alerte: IAlerte = sampleWithRequiredData;
        const alerte2: IAlerte = sampleWithPartialData;
        expectedResult = service.addAlerteToCollectionIfMissing([], alerte, alerte2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alerte);
        expect(expectedResult).toContain(alerte2);
      });

      it('should accept null and undefined values', () => {
        const alerte: IAlerte = sampleWithRequiredData;
        expectedResult = service.addAlerteToCollectionIfMissing([], null, alerte, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alerte);
      });

      it('should return initial array if no Alerte is added', () => {
        const alerteCollection: IAlerte[] = [sampleWithRequiredData];
        expectedResult = service.addAlerteToCollectionIfMissing(alerteCollection, undefined, null);
        expect(expectedResult).toEqual(alerteCollection);
      });
    });

    describe('compareAlerte', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAlerte(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7756 };
        const entity2 = null;

        const compareResult1 = service.compareAlerte(entity1, entity2);
        const compareResult2 = service.compareAlerte(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7756 };
        const entity2 = { id: 29163 };

        const compareResult1 = service.compareAlerte(entity1, entity2);
        const compareResult2 = service.compareAlerte(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7756 };
        const entity2 = { id: 7756 };

        const compareResult1 = service.compareAlerte(entity1, entity2);
        const compareResult2 = service.compareAlerte(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
