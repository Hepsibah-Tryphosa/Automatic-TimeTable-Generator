import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SubjectService } from '../service/subject.service';

import { SubjectComponent } from './subject.component';

describe('Subject Management Component', () => {
  let comp: SubjectComponent;
  let fixture: ComponentFixture<SubjectComponent>;
  let service: SubjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SubjectComponent],
    })
      .overrideTemplate(SubjectComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubjectComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SubjectService);

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
    expect(comp.subjects?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
