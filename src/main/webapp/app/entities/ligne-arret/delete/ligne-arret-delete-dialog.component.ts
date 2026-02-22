import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILigneArret } from '../ligne-arret.model';
import { LigneArretService } from '../service/ligne-arret.service';

@Component({
  templateUrl: './ligne-arret-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LigneArretDeleteDialogComponent {
  ligneArret?: ILigneArret;

  protected ligneArretService = inject(LigneArretService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ligneArretService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
