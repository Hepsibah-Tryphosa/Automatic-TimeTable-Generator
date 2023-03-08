import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILecturer, Lecturer } from '../lecturer.model';

import { LecturerService } from './lecturer.service';

describe('Lecturer Service', () => {
  let service: LecturerService;
  let httpMock: HttpTestingController;
  let elemDefault: ILecturer;
  let expectedResult: ILecturer | ILecturer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LecturerService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      emailId: 'AAAAAAA',
      name: 'AAAAAAA',
      empId: 'AAAAAAA',
      expYears: 0,
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

    it('should create a Lecturer', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Lecturer()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Lecturer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          emailId: 'BBBBBB',
          name: 'BBBBBB',
          empId: 'BBBBBB',
          expYears: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Lecturer', () => {
      const patchObject = Object.assign(
        {
          emailId: 'BBBBBB',
          name: 'BBBBBB',
          empId: 'BBBBBB',
        },
        new Lecturer()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Lecturer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          emailId: 'BBBBBB',
          name: 'BBBBBB',
          empId: 'BBBBBB',
          expYears: 1,
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

    it('should delete a Lecturer', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLecturerToCollectionIfMissing', () => {
      it('should add a Lecturer to an empty array', () => {
        const lecturer: ILecturer = { id: 123 };
        expectedResult = service.addLecturerToCollectionIfMissing([], lecturer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lecturer);
      });

      it('should not add a Lecturer to an array that contains it', () => {
        const lecturer: ILecturer = { id: 123 };
        const lecturerCollection: ILecturer[] = [
          {
            ...lecturer,
          },
          { id: 456 },
        ];
        expectedResult = service.addLecturerToCollectionIfMissing(lecturerCollection, lecturer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Lecturer to an array that doesn't contain it", () => {
        const lecturer: ILecturer = { id: 123 };
        const lecturerCollection: ILecturer[] = [{ id: 456 }];
        expectedResult = service.addLecturerToCollectionIfMissing(lecturerCollection, lecturer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lecturer);
      });

      it('should add only unique Lecturer to an array', () => {
        const lecturerArray: ILecturer[] = [{ id: 123 }, { id: 456 }, { id: 86487 }];
        const lecturerCollection: ILecturer[] = [{ id: 123 }];
        expectedResult = service.addLecturerToCollectionIfMissing(lecturerCollection, ...lecturerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lecturer: ILecturer = { id: 123 };
        const lecturer2: ILecturer = { id: 456 };
        expectedResult = service.addLecturerToCollectionIfMissing([], lecturer, lecturer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lecturer);
        expect(expectedResult).toContain(lecturer2);
      });

      it('should accept null and undefined values', () => {
        const lecturer: ILecturer = { id: 123 };
        expectedResult = service.addLecturerToCollectionIfMissing([], null, lecturer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lecturer);
      });

      it('should return initial array if no Lecturer is added', () => {
        const lecturerCollection: ILecturer[] = [{ id: 123 }];
        expectedResult = service.addLecturerToCollectionIfMissing(lecturerCollection, undefined, null);
        expect(expectedResult).toEqual(lecturerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
