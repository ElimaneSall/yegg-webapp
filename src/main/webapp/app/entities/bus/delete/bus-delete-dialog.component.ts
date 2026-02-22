import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBus } from '../bus.model';
import { BusService } from '../service/bus.service';

@Component({
  templateUrl: './bus-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BusDeleteDialogComponent {
  bus?: IBus;

  protected busService = inject(BusService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.busService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
