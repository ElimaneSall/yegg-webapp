import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITracking } from '../tracking.model';
import { TrackingService } from '../service/tracking.service';

@Component({
  templateUrl: './tracking-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrackingDeleteDialogComponent {
  tracking?: ITracking;

  protected trackingService = inject(TrackingService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trackingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
