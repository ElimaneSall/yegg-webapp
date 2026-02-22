import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBus, NewBus } from '../bus.model';

export type PartialUpdateBus = Partial<IBus> & Pick<IBus, 'id'>;

type RestOf<T extends IBus | NewBus> = Omit<T, 'gpsLastPing' | 'positionUpdatedAt'> & {
  gpsLastPing?: string | null;
  positionUpdatedAt?: string | null;
};

export type RestBus = RestOf<IBus>;

export type NewRestBus = RestOf<NewBus>;

export type PartialUpdateRestBus = RestOf<PartialUpdateBus>;

export type EntityResponseType = HttpResponse<IBus>;
export type EntityArrayResponseType = HttpResponse<IBus[]>;

@Injectable({ providedIn: 'root' })
export class BusService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/buses');

  create(bus: NewBus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bus);
    return this.http.post<RestBus>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bus: IBus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bus);
    return this.http
      .put<RestBus>(`${this.resourceUrl}/${this.getBusIdentifier(bus)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bus: PartialUpdateBus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bus);
    return this.http
      .patch<RestBus>(`${this.resourceUrl}/${this.getBusIdentifier(bus)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBus>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBusIdentifier(bus: Pick<IBus, 'id'>): number {
    return bus.id;
  }

  compareBus(o1: Pick<IBus, 'id'> | null, o2: Pick<IBus, 'id'> | null): boolean {
    return o1 && o2 ? this.getBusIdentifier(o1) === this.getBusIdentifier(o2) : o1 === o2;
  }

  addBusToCollectionIfMissing<Type extends Pick<IBus, 'id'>>(busCollection: Type[], ...busesToCheck: (Type | null | undefined)[]): Type[] {
    const buses: Type[] = busesToCheck.filter(isPresent);
    if (buses.length > 0) {
      const busCollectionIdentifiers = busCollection.map(busItem => this.getBusIdentifier(busItem));
      const busesToAdd = buses.filter(busItem => {
        const busIdentifier = this.getBusIdentifier(busItem);
        if (busCollectionIdentifiers.includes(busIdentifier)) {
          return false;
        }
        busCollectionIdentifiers.push(busIdentifier);
        return true;
      });
      return [...busesToAdd, ...busCollection];
    }
    return busCollection;
  }

  protected convertDateFromClient<T extends IBus | NewBus | PartialUpdateBus>(bus: T): RestOf<T> {
    return {
      ...bus,
      gpsLastPing: bus.gpsLastPing?.toJSON() ?? null,
      positionUpdatedAt: bus.positionUpdatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBus: RestBus): IBus {
    return {
      ...restBus,
      gpsLastPing: restBus.gpsLastPing ? dayjs(restBus.gpsLastPing) : undefined,
      positionUpdatedAt: restBus.positionUpdatedAt ? dayjs(restBus.positionUpdatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBus>): HttpResponse<IBus> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBus[]>): HttpResponse<IBus[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
