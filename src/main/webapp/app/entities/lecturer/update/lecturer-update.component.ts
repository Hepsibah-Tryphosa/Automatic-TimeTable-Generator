import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILecturer, Lecturer } from '../lecturer.model';
import { LecturerService } from '../service/lecturer.service';
import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';

@Component({
  selector: 'jhi-lecturer-update',
  templateUrl: './lecturer-update.component.html',
})
export class LecturerUpdateComponent implements OnInit {
  isSaving = false;

  subjectsSharedCollection: ISubject[] = [];

  editForm = this.fb.group({
    id: [],
    emailId: [
      null,
      [Validators.required, Validators.maxLength(50), Validators.pattern('^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$')],
    ],
    name: [null, [Validators.required, Validators.maxLength(50), Validators.pattern('^[A-Za-z0-9? ]+$')]],
    empId: [null, [Validators.maxLength(50)]],
    expYears: [],
    subjects: [],
  });

  constructor(
    protected lecturerService: LecturerService,
    protected subjectService: SubjectService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lecturer }) => {
      this.updateForm(lecturer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lecturer = this.createFromForm();
    if (lecturer.id !== undefined) {
      this.subscribeToSaveResponse(this.lecturerService.update(lecturer));
    } else {
      this.subscribeToSaveResponse(this.lecturerService.create(lecturer));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILecturer>>): void {
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

  protected updateForm(lecturer: ILecturer): void {
    this.editForm.patchValue({
      id: lecturer.id,
      emailId: lecturer.emailId,
      name: lecturer.name,
      empId: lecturer.empId,
      expYears: lecturer.expYears,
      subjects: lecturer.subjects,
    });

    this.subjectsSharedCollection = this.subjectService.addSubjectToCollectionIfMissing(
      this.subjectsSharedCollection,
      ...(lecturer.subjects ?? [])
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

  protected createFromForm(): ILecturer {
    return {
      ...new Lecturer(),
      id: this.editForm.get(['id'])!.value,
      emailId: this.editForm.get(['emailId'])!.value,
      name: this.editForm.get(['name'])!.value,
      empId: this.editForm.get(['empId'])!.value,
      expYears: this.editForm.get(['expYears'])!.value,
      subjects: this.editForm.get(['subjects'])!.value,
    };
  }
}
