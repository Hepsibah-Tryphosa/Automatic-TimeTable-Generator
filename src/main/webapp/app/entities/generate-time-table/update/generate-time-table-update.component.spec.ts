import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GenerateTimeTableService } from '../service/generate-time-table.service';
import { IGenerateTimeTable, GenerateTimeTable } from '../generate-time-table.model';

import { GenerateTimeTableUpdateComponent } from './generate-time-table-update.component';

describe('GenerateTimeTable Management Update Component', () => {
  let comp: GenerateTimeTableUpdateComponent;
  let fixture: ComponentFixture<GenerateTimeTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let generateTimeTableService: GenerateTimeTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GenerateTimeTableUpdateComponent],
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
      .overrideTemplate(GenerateTimeTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GenerateTimeTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    generateTimeTableService = TestBed.inject(GenerateTimeTableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const generateTimeTable: IGenerateTimeTable = { id: 456 };

      activatedRoute.data = of({ generateTimeTable });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(generateTimeTable));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GenerateTimeTable>>();
      const generateTimeTable = { id: 123 };
      jest.spyOn(generateTimeTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ generateTimeTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: generateTimeTable }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(generateTimeTableService.update).toHaveBeenCalledWith(generateTimeTable);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GenerateTimeTable>>();
      const generateTimeTable = new GenerateTimeTable();
      jest.spyOn(generateTimeTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ generateTimeTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: generateTimeTable }));
      saveSubject.complete();

      // THEN
      expect(generateTimeTableService.create).toHaveBeenCalledWith(generateTimeTable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GenerateTimeTable>>();
      const generateTimeTable = { id: 123 };
      jest.spyOn(generateTimeTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ generateTimeTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(generateTimeTableService.update).toHaveBeenCalledWith(generateTimeTable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
