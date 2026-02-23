import { Component, NgZone, OnInit, inject, signal, computed } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import dayjs from 'dayjs/esm';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { FilterComponent, FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { IAlerte } from '../alerte.model';

import { AlerteService, EntityArrayResponseType } from '../service/alerte.service';
import { AlerteDeleteDialogComponent } from '../delete/alerte-delete-dialog.component';

interface AdvancedFilters {
  typeCible: string;
  statut: string;
  cibleId: string;
  utilisateurId: string;
  seuilMin: number | null;
  seuilMax: number | null;
  dateDebut: string;
  dateFin: string;
}

@Component({
  selector: 'jhi-alerte',
  templateUrl: './alerte.component.html',
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, FormatMediumDatetimePipe, ItemCountComponent],
})
export class AlerteComponent implements OnInit {
  subscription: Subscription | null = null;
  allAlertes = signal<IAlerte[]>([]); // Toutes les alertes chargées
  filteredAlertes = signal<IAlerte[]>([]); // Alertes après filtres locaux

  isLoading = false;
  showFilters = false;
  searchTerm = '';
  selectedAlerte: IAlerte | null = null;

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  // Filtres avancés
  advancedFilters: AdvancedFilters = {
    typeCible: '',
    statut: '',
    cibleId: '',
    utilisateurId: '',
    seuilMin: null,
    seuilMax: null,
    dateDebut: '',
    dateFin: '',
  };

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  // Pagination locale
  paginatedAlertes = computed(() => {
    const start = (this.page - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    return this.filteredAlertes().slice(start, end);
  });

  public readonly router = inject(Router);
  protected readonly alerteService = inject(AlerteService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (item: IAlerte): number => this.alerteService.getAlerteIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  toggleFilters(): void {
    this.showFilters = !this.showFilters;
  }

  onSearchChange(): void {
    setTimeout(() => this.applyLocalFilters(), 300);
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.applyLocalFilters();
  }

  applyFilters(): void {
    // Applique les filtres avancés côté serveur
    const queryParams: any = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };

    if (this.advancedFilters.typeCible) {
      queryParams['typeCible'] = this.advancedFilters.typeCible;
    }
    if (this.advancedFilters.statut) {
      queryParams['statut'] = this.advancedFilters.statut;
    }
    if (this.advancedFilters.cibleId) {
      queryParams['cibleId'] = this.advancedFilters.cibleId;
    }
    if (this.advancedFilters.utilisateurId) {
      queryParams['utilisateurId'] = this.advancedFilters.utilisateurId;
    }
    if (this.advancedFilters.seuilMin) {
      queryParams['seuilMin'] = this.advancedFilters.seuilMin;
    }
    if (this.advancedFilters.seuilMax) {
      queryParams['seuilMax'] = this.advancedFilters.seuilMax;
    }

    // Créer des IFilterOption valides
    const filterOptions: IFilterOption[] = Object.keys(queryParams).map(key => ({
      name: key,
      values: [queryParams[key]],
      nameAsQueryParam: () => key,
    }));

    this.handleNavigation(1, this.sortState(), filterOptions);
  }

  resetFilters(): void {
    this.advancedFilters = {
      typeCible: '',
      statut: '',
      cibleId: '',
      utilisateurId: '',
      seuilMin: null,
      seuilMax: null,
      dateDebut: '',
      dateFin: '',
    };
    this.searchTerm = '';
    this.applyLocalFilters();
    this.load();
  }

  applyLocalFilters(): void {
    let filtered = this.allAlertes();

    // Filtre par recherche textuelle globale
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(
        alerte =>
          alerte.id.toString().includes(term) ||
          (alerte.typeCible?.toLowerCase() || '').includes(term) ||
          (alerte.cibleId?.toString() || '').includes(term) ||
          (alerte.statut?.toLowerCase() || '').includes(term),
      );
    }

    // Filtres avancés locaux
    if (this.advancedFilters.typeCible) {
      filtered = filtered.filter(a => a.typeCible === this.advancedFilters.typeCible);
    }
    if (this.advancedFilters.statut) {
      filtered = filtered.filter(a => a.statut === this.advancedFilters.statut);
    }
    if (this.advancedFilters.cibleId) {
      filtered = filtered.filter(a => (a.cibleId?.toString() || '').includes(this.advancedFilters.cibleId));
    }

    this.filteredAlertes.set(filtered);
    this.page = 1; // Reset à la première page après filtrage
  }

  selectAlerte(alerte: IAlerte): void {
    this.selectedAlerte = this.selectedAlerte?.id === alerte.id ? null : alerte;
  }

  confirmDelete(alerte: IAlerte): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer l'alerte ${alerte.id} ?`)) {
      this.delete(alerte);
    }
  }

  delete(alerte: IAlerte): void {
    const modalRef = this.modalService.open(AlerteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.alerte = alerte;

    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  onItemsPerPageChange(): void {
    this.page = 1;
    this.applyLocalFilters();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event, this.filters.filterOptions);
  }

  navigateToPage(page: number): void {
    this.page = page;
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    this.filters.initializeFromParams(params);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.allAlertes.set(dataFromBody);
    this.applyLocalFilters();
    this.isLoading = false;
  }

  protected fillComponentAttributesFromResponseBody(data: IAlerte[] | null): IAlerte[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
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

    return this.alerteService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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

  // Méthodes utilitaires pour l'affichage avec gestion des valeurs null/undefined

  getStatutClass(statut: string | null | undefined): string {
    switch ((statut || '').toUpperCase()) {
      case 'ACTIF':
        return 'bg-success';
      case 'INACTIF':
        return 'bg-secondary';
      case 'SUSPENDU':
        return 'bg-warning';
      case 'EN_ATTENTE':
        return 'bg-info';
      default:
        return 'bg-primary';
    }
  }

  getTypeCibleClass(typeCible: string | null | undefined): string {
    const type = typeCible || '';
    const colors: Record<string, string> = {
      TYPE1: 'bg-primary',
      TYPE2: 'bg-info',
      TYPE3: 'bg-warning',
    };
    return colors[type] || 'bg-secondary';
  }

  getDeclenchementsClass(nombre: number | null | undefined): string {
    const value = nombre || 0;
    if (value > 100) return 'bg-danger';
    if (value > 50) return 'bg-warning';
    if (value > 10) return 'bg-info';
    return 'bg-success';
  }

  getSeuilProgressClass(seuil: number | null | undefined): string {
    const value = seuil || 0;
    if (value > 100) return 'bg-danger';
    if (value > 60) return 'bg-warning';
    if (value > 30) return 'bg-info';
    return 'bg-success';
  }

  getSeuilPercentage(seuil: number | null | undefined): number {
    const value = seuil || 0;
    // Suppose un seuil maximum de 240 minutes (4 heures)
    return Math.min((value / 240) * 100, 100);
  }

  parseJoursActivation(jours: string | null | undefined): string[] {
    if (!jours) return [];
    return jours.split(',').map(j => j.trim());
  }

  isExpired(alerte: IAlerte): boolean {
    if (!alerte.dernierDeclenchement) return false;
    const now = new Date();
    const lastTrigger = alerte.dernierDeclenchement.toDate(); // Conversion Dayjs -> Date
    const hoursDiff = (now.getTime() - lastTrigger.getTime()) / (1000 * 60 * 60);
    return hoursDiff > 24; // Expiré si plus de 24h sans déclenchement
  }
}
