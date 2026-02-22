import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ITracking } from '../tracking.model';

@Component({
  selector: 'jhi-tracking-detail',
  templateUrl: './tracking-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class TrackingDetailComponent {
  tracking = input<ITracking | null>(null);

  previousState(): void {
    window.history.back();
  }
}
