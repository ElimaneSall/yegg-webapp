import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFavori } from '../favori.model';

@Component({
  selector: 'jhi-favori-detail',
  templateUrl: './favori-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FavoriDetailComponent {
  favori = input<IFavori | null>(null);

  previousState(): void {
    window.history.back();
  }
}
