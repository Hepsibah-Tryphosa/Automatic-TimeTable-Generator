import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IGenerateTimeTable, GenerateTimeTable } from '../generate-time-table.model';
import { GenerateTimeTableService } from '../service/generate-time-table.service';

@Component({
  selector: 'jhi-generate-time-table-update',
  templateUrl: './generate-time-table-update.component.html',
})
export class GenerateTimeTableUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    wokDays: [],
  });

  constructor(
    protected generateTimeTableService: GenerateTimeTableService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ generateTimeTable }) => {
      this.updateForm(generateTimeTable);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const generateTimeTable = this.createFromForm();
    if (generateTimeTable.id !== undefined) {
      this.subscribeToSaveResponse(this.generateTimeTableService.update(generateTimeTable));
    } else {
      this.subscribeToSaveResponse(this.generateTimeTableService.create(generateTimeTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGenerateTimeTable>>): void {
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

  protected updateForm(generateTimeTable: IGenerateTimeTable): void {
    this.editForm.patchValue({
      id: generateTimeTable.id,
      wokDays: generateTimeTable.wokDays,
    });
  }

  protected createFromForm(): IGenerateTimeTable {
    return {
      ...new GenerateTimeTable(),
      id: this.editForm.get(['id'])!.value,
      wokDays: this.editForm.get(['wokDays'])!.value,
    };
  }
}
