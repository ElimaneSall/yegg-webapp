import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ILigneArret } from '../ligne-arret.model';

@Component({
  selector: 'jhi-ligne-arret-detail',
  templateUrl: './ligne-arret-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class LigneArretDetailComponent {
  ligneArret = input<ILigneArret | null>(null);

  previousState(): void {
    window.history.back();
  }
}
