import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SemisterDetailComponent } from './semister-detail.component';

describe('Semister Management Detail Component', () => {
  let comp: SemisterDetailComponent;
  let fixture: ComponentFixture<SemisterDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SemisterDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ semister: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SemisterDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SemisterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load semister on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.semister).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
