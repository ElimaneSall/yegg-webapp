import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILigne } from '../ligne.model';
import { LigneService } from '../service/ligne.service';

@Component({
  templateUrl: './ligne-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LigneDeleteDialogComponent {
  ligne?: ILigne;

  protected ligneService = inject(LigneService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ligneService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
