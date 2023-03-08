import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICollegeInfo, CollegeInfo } from '../college-info.model';
import { CollegeInfoService } from '../service/college-info.service';

import { CollegeInfoRoutingResolveService } from './college-info-routing-resolve.service';

describe('CollegeInfo routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CollegeInfoRoutingResolveService;
  let service: CollegeInfoService;
  let resultCollegeInfo: ICollegeInfo | undefined;

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
    routingResolveService = TestBed.inject(CollegeInfoRoutingResolveService);
    service = TestBed.inject(CollegeInfoService);
    resultCollegeInfo = undefined;
  });

  describe('resolve', () => {
    it('should return ICollegeInfo returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCollegeInfo = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCollegeInfo).toEqual({ id: 123 });
    });

    it('should return new ICollegeInfo if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCollegeInfo = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCollegeInfo).toEqual(new CollegeInfo());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CollegeInfo })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCollegeInfo = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCollegeInfo).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
