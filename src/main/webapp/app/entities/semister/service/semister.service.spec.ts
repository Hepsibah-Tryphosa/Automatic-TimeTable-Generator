import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISemister, Semister } from '../semister.model';

import { SemisterService } from './semister.service';

describe('Semister Service', () => {
  let service: SemisterService;
  let httpMock: HttpTestingController;
  let elemDefault: ISemister;
  let expectedResult: ISemister | ISemister[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SemisterService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a Semister', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Semister()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Semister', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Semister', () => {
      const patchObject = Object.assign({}, new Semister());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Semister', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a Semister', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSemisterToCollectionIfMissing', () => {
      it('should add a Semister to an empty array', () => {
        const semister: ISemister = { id: 123 };
        expectedResult = service.addSemisterToCollectionIfMissing([], semister);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(semister);
      });

      it('should not add a Semister to an array that contains it', () => {
        const semister: ISemister = { id: 123 };
        const semisterCollection: ISemister[] = [
          {
            ...semister,
          },
          { id: 456 },
        ];
        expectedResult = service.addSemisterToCollectionIfMissing(semisterCollection, semister);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Semister to an array that doesn't contain it", () => {
        const semister: ISemister = { id: 123 };
        const semisterCollection: ISemister[] = [{ id: 456 }];
        expectedResult = service.addSemisterToCollectionIfMissing(semisterCollection, semister);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(semister);
      });

      it('should add only unique Semister to an array', () => {
        const semisterArray: ISemister[] = [{ id: 123 }, { id: 456 }, { id: 57812 }];
        const semisterCollection: ISemister[] = [{ id: 123 }];
        expectedResult = service.addSemisterToCollectionIfMissing(semisterCollection, ...semisterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const semister: ISemister = { id: 123 };
        const semister2: ISemister = { id: 456 };
        expectedResult = service.addSemisterToCollectionIfMissing([], semister, semister2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(semister);
        expect(expectedResult).toContain(semister2);
      });

      it('should accept null and undefined values', () => {
        const semister: ISemister = { id: 123 };
        expectedResult = service.addSemisterToCollectionIfMissing([], null, semister, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(semister);
      });

      it('should return initial array if no Semister is added', () => {
        const semisterCollection: ISemister[] = [{ id: 123 }];
        expectedResult = service.addSemisterToCollectionIfMissing(semisterCollection, undefined, null);
        expect(expectedResult).toEqual(semisterCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
