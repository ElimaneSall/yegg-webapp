import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlerte, NewAlerte } from '../alerte.model';

export type PartialUpdateAlerte = Partial<IAlerte> & Pick<IAlerte, 'id'>;

type RestOf<T extends IAlerte | NewAlerte> = Omit<T, 'heureDebut' | 'heureFin' | 'dernierDeclenchement'> & {
  heureDebut?: string | null;
  heureFin?: string | null;
  dernierDeclenchement?: string | null;
};

export type RestAlerte = RestOf<IAlerte>;

export type NewRestAlerte = RestOf<NewAlerte>;

export type PartialUpdateRestAlerte = RestOf<PartialUpdateAlerte>;

export type EntityResponseType = HttpResponse<IAlerte>;
export type EntityArrayResponseType = HttpResponse<IAlerte[]>;

@Injectable({ providedIn: 'root' })
export class AlerteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alertes');

  create(alerte: NewAlerte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alerte);
    return this.http
      .post<RestAlerte>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(alerte: IAlerte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alerte);
    return this.http
      .put<RestAlerte>(`${this.resourceUrl}/${this.getAlerteIdentifier(alerte)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(alerte: PartialUpdateAlerte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alerte);
    return this.http
      .patch<RestAlerte>(`${this.resourceUrl}/${this.getAlerteIdentifier(alerte)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAlerte>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAlerte[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAlerteIdentifier(alerte: Pick<IAlerte, 'id'>): number {
    return alerte.id;
  }

  compareAlerte(o1: Pick<IAlerte, 'id'> | null, o2: Pick<IAlerte, 'id'> | null): boolean {
    return o1 && o2 ? this.getAlerteIdentifier(o1) === this.getAlerteIdentifier(o2) : o1 === o2;
  }

  addAlerteToCollectionIfMissing<Type extends Pick<IAlerte, 'id'>>(
    alerteCollection: Type[],
    ...alertesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const alertes: Type[] = alertesToCheck.filter(isPresent);
    if (alertes.length > 0) {
      const alerteCollectionIdentifiers = alerteCollection.map(alerteItem => this.getAlerteIdentifier(alerteItem));
      const alertesToAdd = alertes.filter(alerteItem => {
        const alerteIdentifier = this.getAlerteIdentifier(alerteItem);
        if (alerteCollectionIdentifiers.includes(alerteIdentifier)) {
          return false;
        }
        alerteCollectionIdentifiers.push(alerteIdentifier);
        return true;
      });
      return [...alertesToAdd, ...alerteCollection];
    }
    return alerteCollection;
  }

  protected convertDateFromClient<T extends IAlerte | NewAlerte | PartialUpdateAlerte>(alerte: T): RestOf<T> {
    return {
      ...alerte,
      heureDebut: alerte.heureDebut?.toJSON() ?? null,
      heureFin: alerte.heureFin?.toJSON() ?? null,
      dernierDeclenchement: alerte.dernierDeclenchement?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAlerte: RestAlerte): IAlerte {
    return {
      ...restAlerte,
      heureDebut: restAlerte.heureDebut ? dayjs(restAlerte.heureDebut) : undefined,
      heureFin: restAlerte.heureFin ? dayjs(restAlerte.heureFin) : undefined,
      dernierDeclenchement: restAlerte.dernierDeclenchement ? dayjs(restAlerte.dernierDeclenchement) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAlerte>): HttpResponse<IAlerte> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAlerte[]>): HttpResponse<IAlerte[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
