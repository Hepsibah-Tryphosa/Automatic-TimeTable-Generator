import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LecturerService } from '../service/lecturer.service';

import { LecturerComponent } from './lecturer.component';

describe('Lecturer Management Component', () => {
  let comp: LecturerComponent;
  let fixture: ComponentFixture<LecturerComponent>;
  let service: LecturerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LecturerComponent],
    })
      .overrideTemplate(LecturerComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LecturerComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LecturerService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.lecturers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
