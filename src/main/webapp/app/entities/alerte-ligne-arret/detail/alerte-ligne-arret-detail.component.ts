import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAlerteLigneArret } from '../alerte-ligne-arret.model';

@Component({
  selector: 'jhi-alerte-ligne-arret-detail',
  templateUrl: './alerte-ligne-arret-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AlerteLigneArretDetailComponent {
  alerteLigneArret = input<IAlerteLigneArret | null>(null);

  previousState(): void {
    window.history.back();
  }
}
