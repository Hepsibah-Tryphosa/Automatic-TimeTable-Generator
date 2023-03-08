import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICollegeInfo, CollegeInfo } from '../college-info.model';

import { CollegeInfoService } from './college-info.service';

describe('CollegeInfo Service', () => {
  let service: CollegeInfoService;
  let httpMock: HttpTestingController;
  let elemDefault: ICollegeInfo;
  let expectedResult: ICollegeInfo | ICollegeInfo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CollegeInfoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      contactNo: 'AAAAAAA',
      address: 'AAAAAAA',
      city: 'AAAAAAA',
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

    it('should create a CollegeInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CollegeInfo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CollegeInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          contactNo: 'BBBBBB',
          address: 'BBBBBB',
          city: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CollegeInfo', () => {
      const patchObject = Object.assign(
        {
          address: 'BBBBBB',
          city: 'BBBBBB',
        },
        new CollegeInfo()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CollegeInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          contactNo: 'BBBBBB',
          address: 'BBBBBB',
          city: 'BBBBBB',
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

    it('should delete a CollegeInfo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCollegeInfoToCollectionIfMissing', () => {
      it('should add a CollegeInfo to an empty array', () => {
        const collegeInfo: ICollegeInfo = { id: 123 };
        expectedResult = service.addCollegeInfoToCollectionIfMissing([], collegeInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(collegeInfo);
      });

      it('should not add a CollegeInfo to an array that contains it', () => {
        const collegeInfo: ICollegeInfo = { id: 123 };
        const collegeInfoCollection: ICollegeInfo[] = [
          {
            ...collegeInfo,
          },
          { id: 456 },
        ];
        expectedResult = service.addCollegeInfoToCollectionIfMissing(collegeInfoCollection, collegeInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CollegeInfo to an array that doesn't contain it", () => {
        const collegeInfo: ICollegeInfo = { id: 123 };
        const collegeInfoCollection: ICollegeInfo[] = [{ id: 456 }];
        expectedResult = service.addCollegeInfoToCollectionIfMissing(collegeInfoCollection, collegeInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(collegeInfo);
      });

      it('should add only unique CollegeInfo to an array', () => {
        const collegeInfoArray: ICollegeInfo[] = [{ id: 123 }, { id: 456 }, { id: 5514 }];
        const collegeInfoCollection: ICollegeInfo[] = [{ id: 123 }];
        expectedResult = service.addCollegeInfoToCollectionIfMissing(collegeInfoCollection, ...collegeInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const collegeInfo: ICollegeInfo = { id: 123 };
        const collegeInfo2: ICollegeInfo = { id: 456 };
        expectedResult = service.addCollegeInfoToCollectionIfMissing([], collegeInfo, collegeInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(collegeInfo);
        expect(expectedResult).toContain(collegeInfo2);
      });

      it('should accept null and undefined values', () => {
        const collegeInfo: ICollegeInfo = { id: 123 };
        expectedResult = service.addCollegeInfoToCollectionIfMissing([], null, collegeInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(collegeInfo);
      });

      it('should return initial array if no CollegeInfo is added', () => {
        const collegeInfoCollection: ICollegeInfo[] = [{ id: 123 }];
        expectedResult = service.addCollegeInfoToCollectionIfMissing(collegeInfoCollection, undefined, null);
        expect(expectedResult).toEqual(collegeInfoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
