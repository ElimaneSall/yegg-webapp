import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IArret } from '../arret.model';
import { ArretService } from '../service/arret.service';

@Component({
  templateUrl: './arret-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ArretDeleteDialogComponent {
  arret?: IArret;

  protected arretService = inject(ArretService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.arretService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
