import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAlerteLigneArret } from '../alerte-ligne-arret.model';
import { AlerteLigneArretService } from '../service/alerte-ligne-arret.service';

@Component({
  templateUrl: './alerte-ligne-arret-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AlerteLigneArretDeleteDialogComponent {
  alerteLigneArret?: IAlerteLigneArret;

  protected alerteLigneArretService = inject(AlerteLigneArretService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alerteLigneArretService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
