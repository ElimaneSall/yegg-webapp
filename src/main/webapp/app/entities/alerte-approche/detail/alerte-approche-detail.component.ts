import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAlerteApproche } from '../alerte-approche.model';

@Component({
  selector: 'jhi-alerte-approche-detail',
  templateUrl: './alerte-approche-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AlerteApprocheDetailComponent {
  alerteApproche = input<IAlerteApproche | null>(null);

  previousState(): void {
    window.history.back();
  }
}
