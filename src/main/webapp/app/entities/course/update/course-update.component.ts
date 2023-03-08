import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICourse, Course } from '../course.model';
import { CourseService } from '../service/course.service';
import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';

@Component({
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html',
})
export class CourseUpdateComponent implements OnInit {
  isSaving = false;

  subjectsSharedCollection: ISubject[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(10)]],
    subjects: [],
  });

  constructor(
    protected courseService: CourseService,
    protected subjectService: SubjectService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ course }) => {
      this.updateForm(course);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const course = this.createFromForm();
    if (course.id !== undefined) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
  }

  trackSubjectById(_index: number, item: ISubject): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>): void {
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

  protected updateForm(course: ICourse): void {
    this.editForm.patchValue({
      id: course.id,
      name: course.name,
      subjects: course.subjects,
    });

    this.subjectsSharedCollection = this.subjectService.addSubjectToCollectionIfMissing(
      this.subjectsSharedCollection,
      ...(course.subjects ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): ICourse {
    return {
      ...new Course(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      subjects: this.editForm.get(['subjects'])!.value,
    };
  }
}
