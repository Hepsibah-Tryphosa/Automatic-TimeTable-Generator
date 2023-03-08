import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SemisterService } from '../service/semister.service';

import { SemisterComponent } from './semister.component';

describe('Semister Management Component', () => {
  let comp: SemisterComponent;
  let fixture: ComponentFixture<SemisterComponent>;
  let service: SemisterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SemisterComponent],
    })
      .overrideTemplate(SemisterComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SemisterComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SemisterService);

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
    expect(comp.semisters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
