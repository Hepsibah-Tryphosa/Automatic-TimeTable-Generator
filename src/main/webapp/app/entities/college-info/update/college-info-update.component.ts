import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICollegeInfo, CollegeInfo } from '../college-info.model';
import { CollegeInfoService } from '../service/college-info.service';

@Component({
  selector: 'jhi-college-info-update',
  templateUrl: './college-info-update.component.html',
})
export class CollegeInfoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    contactNo: [null, [Validators.required, Validators.maxLength(10)]],
    address: [null, [Validators.maxLength(100)]],
    city: [null, [Validators.required, Validators.maxLength(50)]],
  });

  constructor(protected collegeInfoService: CollegeInfoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ collegeInfo }) => {
      this.updateForm(collegeInfo);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const collegeInfo = this.createFromForm();
    if (collegeInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.collegeInfoService.update(collegeInfo));
    } else {
      this.subscribeToSaveResponse(this.collegeInfoService.create(collegeInfo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICollegeInfo>>): void {
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

  protected updateForm(collegeInfo: ICollegeInfo): void {
    this.editForm.patchValue({
      id: collegeInfo.id,
      name: collegeInfo.name,
      contactNo: collegeInfo.contactNo,
      address: collegeInfo.address,
      city: collegeInfo.city,
    });
  }

  protected createFromForm(): ICollegeInfo {
    return {
      ...new CollegeInfo(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      contactNo: this.editForm.get(['contactNo'])!.value,
      address: this.editForm.get(['address'])!.value,
      city: this.editForm.get(['city'])!.value,
    };
  }
}
