import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAlerte } from '../alerte.model';
import { AlerteService } from '../service/alerte.service';

@Component({
  templateUrl: './alerte-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AlerteDeleteDialogComponent {
  alerte?: IAlerte;

  protected alerteService = inject(AlerteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alerteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
