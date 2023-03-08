import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISubject, Subject } from '../subject.model';
import { SubjectService } from '../service/subject.service';

@Component({
  selector: 'jhi-subject-update',
  templateUrl: './subject-update.component.html',
})
export class SubjectUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    reqHrs: [],
  });

  constructor(protected subjectService: SubjectService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subject }) => {
      this.updateForm(subject);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subject = this.createFromForm();
    if (subject.id !== undefined) {
      this.subscribeToSaveResponse(this.subjectService.update(subject));
    } else {
      this.subscribeToSaveResponse(this.subjectService.create(subject));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubject>>): void {
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

  protected updateForm(subject: ISubject): void {
    this.editForm.patchValue({
      id: subject.id,
      name: subject.name,
      reqHrs: subject.reqHrs,
    });
  }

  protected createFromForm(): ISubject {
    return {
      ...new Subject(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      reqHrs: this.editForm.get(['reqHrs'])!.value,
    };
  }
}
