import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IBus } from '../bus.model';

@Component({
  selector: 'jhi-bus-detail',
  templateUrl: './bus-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class BusDetailComponent {
  bus = input<IBus | null>(null);

  previousState(): void {
    window.history.back();
  }
}
