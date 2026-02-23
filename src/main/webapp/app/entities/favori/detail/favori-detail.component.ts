import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IFavori } from '../favori.model';

@Component({
  selector: 'jhi-favori-detail',
  templateUrl: './favori-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class FavoriDetailComponent {
  favori = input<IFavori | null>(null);

  previousState(): void {
    window.history.back();
  }
}
