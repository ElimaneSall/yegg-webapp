import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAlerte } from '../alerte.model';

@Component({
  selector: 'jhi-alerte-detail',
  templateUrl: './alerte-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AlerteDetailComponent {
  alerte = input<IAlerte | null>(null);

  previousState(): void {
    window.history.back();
  }
}
