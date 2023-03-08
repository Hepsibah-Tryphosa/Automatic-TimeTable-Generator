import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IGenerateTimeTable, GenerateTimeTable } from '../generate-time-table.model';
import { GenerateTimeTableService } from '../service/generate-time-table.service';

import { GenerateTimeTableRoutingResolveService } from './generate-time-table-routing-resolve.service';

describe('GenerateTimeTable routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: GenerateTimeTableRoutingResolveService;
  let service: GenerateTimeTableService;
  let resultGenerateTimeTable: IGenerateTimeTable | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(GenerateTimeTableRoutingResolveService);
    service = TestBed.inject(GenerateTimeTableService);
    resultGenerateTimeTable = undefined;
  });

  describe('resolve', () => {
    it('should return IGenerateTimeTable returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGenerateTimeTable = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGenerateTimeTable).toEqual({ id: 123 });
    });

    it('should return new IGenerateTimeTable if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGenerateTimeTable = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultGenerateTimeTable).toEqual(new GenerateTimeTable());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as GenerateTimeTable })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGenerateTimeTable = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGenerateTimeTable).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
