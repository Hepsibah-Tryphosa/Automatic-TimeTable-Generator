import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISemister, Semister } from '../semister.model';
import { SemisterService } from '../service/semister.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';

@Component({
  selector: 'jhi-semister-update',
  templateUrl: './semister-update.component.html',
})
export class SemisterUpdateComponent implements OnInit {
  isSaving = false;

  coursesSharedCollection: ICourse[] = [];
  subjectsSharedCollection: ISubject[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(10)]],
    courses: [],
    subjects: [],
  });

  constructor(
    protected semisterService: SemisterService,
    protected courseService: CourseService,
    protected subjectService: SubjectService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ semister }) => {
      this.updateForm(semister);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const semister = this.createFromForm();
    if (semister.id !== undefined) {
      this.subscribeToSaveResponse(this.semisterService.update(semister));
    } else {
      this.subscribeToSaveResponse(this.semisterService.create(semister));
    }
  }

  trackCourseById(_index: number, item: ICourse): number {
    return item.id!;
  }

  trackSubjectById(_index: number, item: ISubject): number {
    return item.id!;
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

  getSelectedSubject(option: ISubject, selectedVals?: ISubject[]): ISubject {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISemister>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(semister: ISemister): void {
    this.editForm.patchValue({
      id: semister.id,
      name: semister.name,
      courses: semister.courses,
      subjects: semister.subjects,
    });

    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing(
      this.coursesSharedCollection,
      ...(semister.courses ?? [])
    );
    this.subjectsSharedCollection = this.subjectService.addSubjectToCollectionIfMissing(
      this.subjectsSharedCollection,
      ...(semister.subjects ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(
        map((courses: ICourse[]) =>
          this.courseService.addCourseToCollectionIfMissing(courses, ...(this.editForm.get('courses')!.value ?? []))
        )
      )
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));

    this.subjectService
      .query()
      .pipe(map((res: HttpResponse<ISubject[]>) => res.body ?? []))
      .pipe(
        map((subjects: ISubject[]) =>
          this.subjectService.addSubjectToCollectionIfMissing(subjects, ...(this.editForm.get('subjects')!.value ?? []))
        )
      )
      .subscribe((subjects: ISubject[]) => (this.subjectsSharedCollection = subjects));
  }

  protected createFromForm(): ISemister {
    return {
      ...new Semister(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      courses: this.editForm.get(['courses'])!.value,
      subjects: this.editForm.get(['subjects'])!.value,
    };
  }
}
