import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CollegeInfoDetailComponent } from './college-info-detail.component';

describe('CollegeInfo Management Detail Component', () => {
  let comp: CollegeInfoDetailComponent;
  let fixture: ComponentFixture<CollegeInfoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CollegeInfoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ collegeInfo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CollegeInfoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CollegeInfoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load collegeInfo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.collegeInfo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
