import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGenerateTimeTable, GenerateTimeTable } from '../generate-time-table.model';

import { GenerateTimeTableService } from './generate-time-table.service';

describe('GenerateTimeTable Service', () => {
  let service: GenerateTimeTableService;
  let httpMock: HttpTestingController;
  let elemDefault: IGenerateTimeTable;
  let expectedResult: IGenerateTimeTable | IGenerateTimeTable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GenerateTimeTableService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      wokDays: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a GenerateTimeTable', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new GenerateTimeTable()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GenerateTimeTable', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          wokDays: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GenerateTimeTable', () => {
      const patchObject = Object.assign({}, new GenerateTimeTable());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GenerateTimeTable', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          wokDays: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a GenerateTimeTable', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGenerateTimeTableToCollectionIfMissing', () => {
      it('should add a GenerateTimeTable to an empty array', () => {
        const generateTimeTable: IGenerateTimeTable = { id: 123 };
        expectedResult = service.addGenerateTimeTableToCollectionIfMissing([], generateTimeTable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(generateTimeTable);
      });

      it('should not add a GenerateTimeTable to an array that contains it', () => {
        const generateTimeTable: IGenerateTimeTable = { id: 123 };
        const generateTimeTableCollection: IGenerateTimeTable[] = [
          {
            ...generateTimeTable,
          },
          { id: 456 },
        ];
        expectedResult = service.addGenerateTimeTableToCollectionIfMissing(generateTimeTableCollection, generateTimeTable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GenerateTimeTable to an array that doesn't contain it", () => {
        const generateTimeTable: IGenerateTimeTable = { id: 123 };
        const generateTimeTableCollection: IGenerateTimeTable[] = [{ id: 456 }];
        expectedResult = service.addGenerateTimeTableToCollectionIfMissing(generateTimeTableCollection, generateTimeTable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(generateTimeTable);
      });

      it('should add only unique GenerateTimeTable to an array', () => {
        const generateTimeTableArray: IGenerateTimeTable[] = [{ id: 123 }, { id: 456 }, { id: 30136 }];
        const generateTimeTableCollection: IGenerateTimeTable[] = [{ id: 123 }];
        expectedResult = service.addGenerateTimeTableToCollectionIfMissing(generateTimeTableCollection, ...generateTimeTableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const generateTimeTable: IGenerateTimeTable = { id: 123 };
        const generateTimeTable2: IGenerateTimeTable = { id: 456 };
        expectedResult = service.addGenerateTimeTableToCollectionIfMissing([], generateTimeTable, generateTimeTable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(generateTimeTable);
        expect(expectedResult).toContain(generateTimeTable2);
      });

      it('should accept null and undefined values', () => {
        const generateTimeTable: IGenerateTimeTable = { id: 123 };
        expectedResult = service.addGenerateTimeTableToCollectionIfMissing([], null, generateTimeTable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(generateTimeTable);
      });

      it('should return initial array if no GenerateTimeTable is added', () => {
        const generateTimeTableCollection: IGenerateTimeTable[] = [{ id: 123 }];
        expectedResult = service.addGenerateTimeTableToCollectionIfMissing(generateTimeTableCollection, undefined, null);
        expect(expectedResult).toEqual(generateTimeTableCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
