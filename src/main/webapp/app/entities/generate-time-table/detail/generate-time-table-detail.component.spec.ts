import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GenerateTimeTableDetailComponent } from './generate-time-table-detail.component';

describe('GenerateTimeTable Management Detail Component', () => {
  let comp: GenerateTimeTableDetailComponent;
  let fixture: ComponentFixture<GenerateTimeTableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GenerateTimeTableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ generateTimeTable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GenerateTimeTableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GenerateTimeTableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load generateTimeTable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.generateTimeTable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
