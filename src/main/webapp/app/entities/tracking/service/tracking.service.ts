import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITracking, NewTracking } from '../tracking.model';

export type PartialUpdateTracking = Partial<ITracking> & Pick<ITracking, 'id'>;

type RestOf<T extends ITracking | NewTracking> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestTracking = RestOf<ITracking>;

export type NewRestTracking = RestOf<NewTracking>;

export type PartialUpdateRestTracking = RestOf<PartialUpdateTracking>;

export type EntityResponseType = HttpResponse<ITracking>;
export type EntityArrayResponseType = HttpResponse<ITracking[]>;

@Injectable({ providedIn: 'root' })
export class TrackingService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trackings');

  create(tracking: NewTracking): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tracking);
    return this.http
      .post<RestTracking>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tracking: ITracking): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tracking);
    return this.http
      .put<RestTracking>(`${this.resourceUrl}/${this.getTrackingIdentifier(tracking)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tracking: PartialUpdateTracking): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tracking);
    return this.http
      .patch<RestTracking>(`${this.resourceUrl}/${this.getTrackingIdentifier(tracking)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTracking>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTracking[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrackingIdentifier(tracking: Pick<ITracking, 'id'>): number {
    return tracking.id;
  }

  compareTracking(o1: Pick<ITracking, 'id'> | null, o2: Pick<ITracking, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrackingIdentifier(o1) === this.getTrackingIdentifier(o2) : o1 === o2;
  }

  addTrackingToCollectionIfMissing<Type extends Pick<ITracking, 'id'>>(
    trackingCollection: Type[],
    ...trackingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trackings: Type[] = trackingsToCheck.filter(isPresent);
    if (trackings.length > 0) {
      const trackingCollectionIdentifiers = trackingCollection.map(trackingItem => this.getTrackingIdentifier(trackingItem));
      const trackingsToAdd = trackings.filter(trackingItem => {
        const trackingIdentifier = this.getTrackingIdentifier(trackingItem);
        if (trackingCollectionIdentifiers.includes(trackingIdentifier)) {
          return false;
        }
        trackingCollectionIdentifiers.push(trackingIdentifier);
        return true;
      });
      return [...trackingsToAdd, ...trackingCollection];
    }
    return trackingCollection;
  }

  protected convertDateFromClient<T extends ITracking | NewTracking | PartialUpdateTracking>(tracking: T): RestOf<T> {
    return {
      ...tracking,
      timestamp: tracking.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTracking: RestTracking): ITracking {
    return {
      ...restTracking,
      timestamp: restTracking.timestamp ? dayjs(restTracking.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTracking>): HttpResponse<ITracking> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTracking[]>): HttpResponse<ITracking[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
