import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAlerteApproche } from '../alerte-approche.model';
import { AlerteApprocheService } from '../service/alerte-approche.service';

@Component({
  templateUrl: './alerte-approche-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AlerteApprocheDeleteDialogComponent {
  alerteApproche?: IAlerteApproche;

  protected alerteApprocheService = inject(AlerteApprocheService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alerteApprocheService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
