import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBus } from 'app/entities/bus/bus.model';
import { BusService } from 'app/entities/bus/service/bus.service';
import { ITracking } from '../tracking.model';
import { TrackingService } from '../service/tracking.service';
import { TrackingFormGroup, TrackingFormService } from './tracking-form.service';

@Component({
  selector: 'jhi-tracking-update',
  templateUrl: './tracking-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrackingUpdateComponent implements OnInit {
  isSaving = false;
  tracking: ITracking | null = null;

  busesSharedCollection: IBus[] = [];

  protected trackingService = inject(TrackingService);
  protected trackingFormService = inject(TrackingFormService);
  protected busService = inject(BusService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrackingFormGroup = this.trackingFormService.createTrackingFormGroup();

  compareBus = (o1: IBus | null, o2: IBus | null): boolean => this.busService.compareBus(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tracking }) => {
      this.tracking = tracking;
      if (tracking) {
        this.updateForm(tracking);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tracking = this.trackingFormService.getTracking(this.editForm);
    if (tracking.id !== null) {
      this.subscribeToSaveResponse(this.trackingService.update(tracking));
    } else {
      this.subscribeToSaveResponse(this.trackingService.create(tracking));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITracking>>): void {
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

  protected updateForm(tracking: ITracking): void {
    this.tracking = tracking;
    this.trackingFormService.resetForm(this.editForm, tracking);

    this.busesSharedCollection = this.busService.addBusToCollectionIfMissing<IBus>(this.busesSharedCollection, tracking.bus);
  }

  protected loadRelationshipsOptions(): void {
    this.busService
      .query()
      .pipe(map((res: HttpResponse<IBus[]>) => res.body ?? []))
      .pipe(map((buses: IBus[]) => this.busService.addBusToCollectionIfMissing<IBus>(buses, this.tracking?.bus)))
      .subscribe((buses: IBus[]) => (this.busesSharedCollection = buses));
  }
}
