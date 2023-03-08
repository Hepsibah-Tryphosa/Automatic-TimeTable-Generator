import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GenerateTimeTableService } from '../service/generate-time-table.service';

import { GenerateTimeTableComponent } from './generate-time-table.component';

describe('GenerateTimeTable Management Component', () => {
  let comp: GenerateTimeTableComponent;
  let fixture: ComponentFixture<GenerateTimeTableComponent>;
  let service: GenerateTimeTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GenerateTimeTableComponent],
    })
      .overrideTemplate(GenerateTimeTableComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GenerateTimeTableComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(GenerateTimeTableService);

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
    expect(comp.generateTimeTables?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
