import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IHistoriqueAlerte } from '../historique-alerte.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../historique-alerte.test-samples';

import { HistoriqueAlerteService, RestHistoriqueAlerte } from './historique-alerte.service';

const requireRestSample: RestHistoriqueAlerte = {
  ...sampleWithRequiredData,
  dateDeclenchement: sampleWithRequiredData.dateDeclenchement?.toJSON(),
  dateLecture: sampleWithRequiredData.dateLecture?.toJSON(),
};

describe('HistoriqueAlerte Service', () => {
  let service: HistoriqueAlerteService;
  let httpMock: HttpTestingController;
  let expectedResult: IHistoriqueAlerte | IHistoriqueAlerte[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(HistoriqueAlerteService);
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

    it('should create a HistoriqueAlerte', () => {
      const historiqueAlerte = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(historiqueAlerte).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HistoriqueAlerte', () => {
      const historiqueAlerte = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(historiqueAlerte).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HistoriqueAlerte', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HistoriqueAlerte', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HistoriqueAlerte', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHistoriqueAlerteToCollectionIfMissing', () => {
      it('should add a HistoriqueAlerte to an empty array', () => {
        const historiqueAlerte: IHistoriqueAlerte = sampleWithRequiredData;
        expectedResult = service.addHistoriqueAlerteToCollectionIfMissing([], historiqueAlerte);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historiqueAlerte);
      });

      it('should not add a HistoriqueAlerte to an array that contains it', () => {
        const historiqueAlerte: IHistoriqueAlerte = sampleWithRequiredData;
        const historiqueAlerteCollection: IHistoriqueAlerte[] = [
          {
            ...historiqueAlerte,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHistoriqueAlerteToCollectionIfMissing(historiqueAlerteCollection, historiqueAlerte);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HistoriqueAlerte to an array that doesn't contain it", () => {
        const historiqueAlerte: IHistoriqueAlerte = sampleWithRequiredData;
        const historiqueAlerteCollection: IHistoriqueAlerte[] = [sampleWithPartialData];
        expectedResult = service.addHistoriqueAlerteToCollectionIfMissing(historiqueAlerteCollection, historiqueAlerte);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historiqueAlerte);
      });

      it('should add only unique HistoriqueAlerte to an array', () => {
        const historiqueAlerteArray: IHistoriqueAlerte[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const historiqueAlerteCollection: IHistoriqueAlerte[] = [sampleWithRequiredData];
        expectedResult = service.addHistoriqueAlerteToCollectionIfMissing(historiqueAlerteCollection, ...historiqueAlerteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const historiqueAlerte: IHistoriqueAlerte = sampleWithRequiredData;
        const historiqueAlerte2: IHistoriqueAlerte = sampleWithPartialData;
        expectedResult = service.addHistoriqueAlerteToCollectionIfMissing([], historiqueAlerte, historiqueAlerte2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historiqueAlerte);
        expect(expectedResult).toContain(historiqueAlerte2);
      });

      it('should accept null and undefined values', () => {
        const historiqueAlerte: IHistoriqueAlerte = sampleWithRequiredData;
        expectedResult = service.addHistoriqueAlerteToCollectionIfMissing([], null, historiqueAlerte, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historiqueAlerte);
      });

      it('should return initial array if no HistoriqueAlerte is added', () => {
        const historiqueAlerteCollection: IHistoriqueAlerte[] = [sampleWithRequiredData];
        expectedResult = service.addHistoriqueAlerteToCollectionIfMissing(historiqueAlerteCollection, undefined, null);
        expect(expectedResult).toEqual(historiqueAlerteCollection);
      });
    });

    describe('compareHistoriqueAlerte', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHistoriqueAlerte(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4252 };
        const entity2 = null;

        const compareResult1 = service.compareHistoriqueAlerte(entity1, entity2);
        const compareResult2 = service.compareHistoriqueAlerte(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4252 };
        const entity2 = { id: 14561 };

        const compareResult1 = service.compareHistoriqueAlerte(entity1, entity2);
        const compareResult2 = service.compareHistoriqueAlerte(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 4252 };
        const entity2 = { id: 4252 };

        const compareResult1 = service.compareHistoriqueAlerte(entity1, entity2);
        const compareResult2 = service.compareHistoriqueAlerte(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
