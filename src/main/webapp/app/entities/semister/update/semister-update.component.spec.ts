import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SemisterService } from '../service/semister.service';
import { ISemister, Semister } from '../semister.model';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';

import { SemisterUpdateComponent } from './semister-update.component';

describe('Semister Management Update Component', () => {
  let comp: SemisterUpdateComponent;
  let fixture: ComponentFixture<SemisterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let semisterService: SemisterService;
  let courseService: CourseService;
  let subjectService: SubjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SemisterUpdateComponent],
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
      .overrideTemplate(SemisterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SemisterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    semisterService = TestBed.inject(SemisterService);
    courseService = TestBed.inject(CourseService);
    subjectService = TestBed.inject(SubjectService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Course query and add missing value', () => {
      const semister: ISemister = { id: 456 };
      const courses: ICourse[] = [{ id: 89934 }];
      semister.courses = courses;

      const courseCollection: ICourse[] = [{ id: 15276 }];
      jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
      const additionalCourses = [...courses];
      const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
      jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ semister });
      comp.ngOnInit();

      expect(courseService.query).toHaveBeenCalled();
      expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(courseCollection, ...additionalCourses);
      expect(comp.coursesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Subject query and add missing value', () => {
      const semister: ISemister = { id: 456 };
      const subjects: ISubject[] = [{ id: 20103 }];
      semister.subjects = subjects;

      const subjectCollection: ISubject[] = [{ id: 62197 }];
      jest.spyOn(subjectService, 'query').mockReturnValue(of(new HttpResponse({ body: subjectCollection })));
      const additionalSubjects = [...subjects];
      const expectedCollection: ISubject[] = [...additionalSubjects, ...subjectCollection];
      jest.spyOn(subjectService, 'addSubjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ semister });
      comp.ngOnInit();

      expect(subjectService.query).toHaveBeenCalled();
      expect(subjectService.addSubjectToCollectionIfMissing).toHaveBeenCalledWith(subjectCollection, ...additionalSubjects);
      expect(comp.subjectsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const semister: ISemister = { id: 456 };
      const courses: ICourse = { id: 89479 };
      semister.courses = [courses];
      const subjects: ISubject = { id: 8550 };
      semister.subjects = [subjects];

      activatedRoute.data = of({ semister });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(semister));
      expect(comp.coursesSharedCollection).toContain(courses);
      expect(comp.subjectsSharedCollection).toContain(subjects);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Semister>>();
      const semister = { id: 123 };
      jest.spyOn(semisterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ semister });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: semister }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(semisterService.update).toHaveBeenCalledWith(semister);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Semister>>();
      const semister = new Semister();
      jest.spyOn(semisterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ semister });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: semister }));
      saveSubject.complete();

      // THEN
      expect(semisterService.create).toHaveBeenCalledWith(semister);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Semister>>();
      const semister = { id: 123 };
      jest.spyOn(semisterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ semister });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(semisterService.update).toHaveBeenCalledWith(semister);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCourseById', () => {
      it('Should return tracked Course primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCourseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSubjectById', () => {
      it('Should return tracked Subject primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSubjectById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedCourse', () => {
      it('Should return option if no Course is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedCourse(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Course for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedCourse(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Course is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedCourse(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedSubject', () => {
      it('Should return option if no Subject is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedSubject(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Subject for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedSubject(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Subject is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedSubject(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
