import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CollegeInfoService } from '../service/college-info.service';

import { CollegeInfoComponent } from './college-info.component';

describe('CollegeInfo Management Component', () => {
  let comp: CollegeInfoComponent;
  let fixture: ComponentFixture<CollegeInfoComponent>;
  let service: CollegeInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CollegeInfoComponent],
    })
      .overrideTemplate(CollegeInfoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CollegeInfoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CollegeInfoService);

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
    expect(comp.collegeInfos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
