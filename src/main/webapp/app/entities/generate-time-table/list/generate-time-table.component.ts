import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, Validators } from '@angular/forms';
import { IGenerateTimeTable } from '../generate-time-table.model';
import { GenerateTimeTableService } from '../service/generate-time-table.service';
import { GenerateTimeTableDeleteDialogComponent } from '../delete/generate-time-table-delete-dialog.component';
import { IDailyTimeTable } from '../generate-weekly-timetable.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { SemisterService } from 'app/entities/semister/service/semister.service';
import { ICourse } from 'app/entities/course/course.model';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { ISemister } from 'app/entities/semister/semister.model';
@Component({
  selector: 'jhi-generate-time-table',
  templateUrl: './generate-time-table.component.html',
  styleUrls: ['./generate-time-table.component.css'],
})
export class GenerateTimeTableComponent implements OnInit {
  generateTimeTables?: IDailyTimeTable[];
  headersTimeTable?: string[];
  coursesSharedCollection: ICourse[] = [];
  semisters?: ISemister[];
  isLoading = false;
  isPrint = false;
  editForm = this.fb.group({
    id: [],
    coursename: [null, [Validators.maxLength(10)]],
    semistername: [null, [Validators.maxLength(10)]],
    classesPerDay: [null, [Validators.required, Validators.maxLength(10)]],
    courses: [],
  });
  constructor(
    protected semisterService: SemisterService,
    protected courseService: CourseService,
    protected generateTimeTableService: GenerateTimeTableService,
    protected modalService: NgbModal,
    protected fb: FormBuilder
  ) {}

  loadAll(courseName: string, semName: string, noOfClassesPerDay: string): void {
    this.isLoading = true;

    this.generateTimeTableService.generateTimetable(courseName, semName, noOfClassesPerDay).subscribe({
      next: (res: HttpResponse<IDailyTimeTable[]>) => {
        this.isLoading = false;
        this.generateTimeTables = res.body ?? [];
        // const cpd = this.generateTimeTables[0].noOfClassesPerDay ?? 0;
        const cpd = 7;
        this.headersTimeTable = new Array(cpd);
        const hashmap = new Map();
        hashmap.set(1, '9:30 AM - 10:20 AM');
        hashmap.set(2, '10:20 AM - 11:10 AM');
        hashmap.set(3, '11:10 AM - 12:00 PM');
        hashmap.set(4, '12:00 PM - 12:50 PM');
        hashmap.set(5, '12:50 PM - 1:30 PM');
        hashmap.set(6, '1:30 PM - 2:20 PM');
        hashmap.set(7, '2:20 PM - 3:10 PM');
        hashmap.set(8, '3:10 PM - 4:00 PM');

        for (let i = 0; i < cpd; i++) {
          this.headersTimeTable[i] = hashmap.get(i + 1);
        }
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll('ECE', '1-1', '7');
    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(
        map((courses: ICourse[]) =>
          this.courseService.addCourseToCollectionIfMissing(courses, ...(this.editForm.get('courses')!.value ?? []))
        )
      )
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));

    this.semisterService.query().subscribe({
      next: (res: HttpResponse<ISemister[]>) => {
        this.isLoading = false;
        this.semisters = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  trackId(_index: number, item: IGenerateTimeTable): number {
    return item.id!;
  }
  trackCourseById(_index: number, item: ICourse): number {
    return item.id!;
  }

  trackSemseterById(_index: number, item: ISemister): number {
    return item.id!;
  }

  getSelectedSemester(option: ISemister, selectedVals?: ISemister[]): ICourse {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedCourse(option: ICourse, selectedVals?: ICourse[]): ICourse {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }
  delete(generateTimeTable: IGenerateTimeTable): void {
    const modalRef = this.modalService.open(GenerateTimeTableDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.generateTimeTable = generateTimeTable;
    // unsubscribe not needed because closed completes on modal close
  }

  SaveToPdf(): void {
    // this.isPrint = true;
    const printContents = document.getElementById('printarea')!.innerHTML;
    const originalContents = document.body.innerHTML;
    document.title = 'Time Table';
    document.body.innerHTML = printContents;
    window.window.print();
    document.body.innerHTML = originalContents;
    // this.isPrint = false;
  }

  editFormSubmit(): void {
    // this.loadAll('ECE', '1-1', '6');
    const smnm = (this.editForm.get(['semistername'])!.value as ISemister).name!;
    // print();
    this.loadAll(
      (this.editForm.get(['coursename'])!.value as ICourse).name!,
      // 'ECE',
      // '1-1',
      smnm,
      this.editForm.get(['classesPerDay'])!.value
    );
  }
}
