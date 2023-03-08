import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LecturerDetailComponent } from './lecturer-detail.component';

describe('Lecturer Management Detail Component', () => {
  let comp: LecturerDetailComponent;
  let fixture: ComponentFixture<LecturerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LecturerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ lecturer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LecturerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LecturerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load lecturer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.lecturer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
