import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CollegeInfoService } from '../service/college-info.service';
import { ICollegeInfo, CollegeInfo } from '../college-info.model';

import { CollegeInfoUpdateComponent } from './college-info-update.component';

describe('CollegeInfo Management Update Component', () => {
  let comp: CollegeInfoUpdateComponent;
  let fixture: ComponentFixture<CollegeInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let collegeInfoService: CollegeInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CollegeInfoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CollegeInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CollegeInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    collegeInfoService = TestBed.inject(CollegeInfoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const collegeInfo: ICollegeInfo = { id: 456 };

      activatedRoute.data = of({ collegeInfo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(collegeInfo));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollegeInfo>>();
      const collegeInfo = { id: 123 };
      jest.spyOn(collegeInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collegeInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collegeInfo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(collegeInfoService.update).toHaveBeenCalledWith(collegeInfo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollegeInfo>>();
      const collegeInfo = new CollegeInfo();
      jest.spyOn(collegeInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collegeInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collegeInfo }));
      saveSubject.complete();

      // THEN
      expect(collegeInfoService.create).toHaveBeenCalledWith(collegeInfo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollegeInfo>>();
      const collegeInfo = { id: 123 };
      jest.spyOn(collegeInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collegeInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(collegeInfoService.update).toHaveBeenCalledWith(collegeInfo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
