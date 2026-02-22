import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFavori } from '../favori.model';
import { FavoriService } from '../service/favori.service';

@Component({
  templateUrl: './favori-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FavoriDeleteDialogComponent {
  favori?: IFavori;

  protected favoriService = inject(FavoriService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.favoriService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
