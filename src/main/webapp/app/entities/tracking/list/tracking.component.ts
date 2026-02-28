import { Component, NgZone, OnInit, inject, signal, OnDestroy } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import { FilterComponent, FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { EntityArrayResponseType, TrackingService } from '../service/tracking.service';
import { TrackingDeleteDialogComponent } from '../delete/tracking-delete-dialog.component';
import { ITracking } from '../tracking.model';
import { BusWebsocketService } from '../service/bus-websocket.service.service';
import SharedModule from '../../../shared/shared.module';

@Component({
  selector: 'jhi-tracking',
  templateUrl: './tracking.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    FormatMediumDatetimePipe,
    FilterComponent,
    ItemCountComponent,
  ],
})
export class TrackingComponent implements OnInit, OnDestroy {
  subscription: Subscription | null = null;
  trackings = signal<ITracking[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public readonly router = inject(Router);
  protected readonly trackingService = inject(TrackingService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected dataUtils = inject(DataUtils);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected readonly busWebsocketService = inject(BusWebsocketService);
  private websocketSubscription?: Subscription;

  trackId = (item: ITracking): number => this.trackingService.getTrackingIdentifier(item);

  ngOnInit(): void {
    // 1. Charge les données initiales
    this.load();

    // 2. S'abonne au flux temps réel (WebSocket)
    this.websocketSubscription = this.busWebsocketService.subscribeToBusPositions().subscribe(newPosition => {
      this.updateTrackingList(newPosition);
    });

    // 3. Gère les changements de route (pagination, filtres, tri)
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));
  }

  /**
   * Met à jour la liste des trackings en temps réel
   */
  private updateTrackingList(newPosition: any): void {
    this.trackings.update(currentTrackings => {
      // Si c'est un nouveau tracking (pas dans la liste actuelle)
      const index = currentTrackings.findIndex(t => t.bus?.id === newPosition.busId);

      /*  if (index !== -1) {
        // Met à jour le tracking existant
        const updated = [...currentTrackings];
        updated[index] = {
          ...updated[index],
          latitude: newPosition.latitude,
          longitude: newPosition.longitude,
          vitesse: newPosition.vitesse,
          timestamp: new Date() // ou utilisez le timestamp du message
        };
        return updated;
      }*/

      // Ajoute le nouveau tracking en tête de liste
      // Mais attention : ça peut créer des doublons si on recharge la page
      // On pourrait plutôt charger le détail du bus depuis l'API
      return [newPosition, ...currentTrackings];
    });

    // Force la détection de changements (optionnel car signals le fait déjà)
    this.ngZone.run(() => {});
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(tracking: ITracking): void {
    const modalRef = this.modalService.open(TrackingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tracking = tracking;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event, this.filters.filterOptions);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState(), this.filters.filterOptions);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get('page');
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get('sort') ?? data['defaultSort']));
    this.filters.initializeFromParams(params);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.trackings.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: ITracking[] | null): ITracking[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page, filters } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    filters.filterOptions.forEach(filterOption => {
      queryObject[filterOption.name] = filterOption.values;
    });
    return this.trackingService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    filterOptions?.forEach(filterOption => {
      queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
    });

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }

  ngOnDestroy(): void {
    this.websocketSubscription?.unsubscribe();
    this.subscription?.unsubscribe();
  }
}
