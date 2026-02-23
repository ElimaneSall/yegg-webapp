import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlerteApproche, NewAlerteApproche } from '../alerte-approche.model';

export type PartialUpdateAlerteApproche = Partial<IAlerteApproche> & Pick<IAlerteApproche, 'id'>;

type RestOf<T extends IAlerteApproche | NewAlerteApproche> = Omit<T, 'dateCreation' | 'dateModification' | 'dernierDeclenchement'> & {
  dateCreation?: string | null;
  dateModification?: string | null;
  dernierDeclenchement?: string | null;
};

export type RestAlerteApproche = RestOf<IAlerteApproche>;

export type NewRestAlerteApproche = RestOf<NewAlerteApproche>;

export type PartialUpdateRestAlerteApproche = RestOf<PartialUpdateAlerteApproche>;

export type EntityResponseType = HttpResponse<IAlerteApproche>;
export type EntityArrayResponseType = HttpResponse<IAlerteApproche[]>;

@Injectable({ providedIn: 'root' })
export class AlerteApprocheService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alerte-approches');

  create(alerteApproche: NewAlerteApproche): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alerteApproche);
    return this.http
      .post<RestAlerteApproche>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(alerteApproche: IAlerteApproche): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alerteApproche);
    return this.http
      .put<RestAlerteApproche>(`${this.resourceUrl}/${this.getAlerteApprocheIdentifier(alerteApproche)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(alerteApproche: PartialUpdateAlerteApproche): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alerteApproche);
    return this.http
      .patch<RestAlerteApproche>(`${this.resourceUrl}/${this.getAlerteApprocheIdentifier(alerteApproche)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAlerteApproche>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAlerteApproche[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAlerteApprocheIdentifier(alerteApproche: Pick<IAlerteApproche, 'id'>): number {
    return alerteApproche.id;
  }

  compareAlerteApproche(o1: Pick<IAlerteApproche, 'id'> | null, o2: Pick<IAlerteApproche, 'id'> | null): boolean {
    return o1 && o2 ? this.getAlerteApprocheIdentifier(o1) === this.getAlerteApprocheIdentifier(o2) : o1 === o2;
  }

  addAlerteApprocheToCollectionIfMissing<Type extends Pick<IAlerteApproche, 'id'>>(
    alerteApprocheCollection: Type[],
    ...alerteApprochesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const alerteApproches: Type[] = alerteApprochesToCheck.filter(isPresent);
    if (alerteApproches.length > 0) {
      const alerteApprocheCollectionIdentifiers = alerteApprocheCollection.map(alerteApprocheItem =>
        this.getAlerteApprocheIdentifier(alerteApprocheItem),
      );
      const alerteApprochesToAdd = alerteApproches.filter(alerteApprocheItem => {
        const alerteApprocheIdentifier = this.getAlerteApprocheIdentifier(alerteApprocheItem);
        if (alerteApprocheCollectionIdentifiers.includes(alerteApprocheIdentifier)) {
          return false;
        }
        alerteApprocheCollectionIdentifiers.push(alerteApprocheIdentifier);
        return true;
      });
      return [...alerteApprochesToAdd, ...alerteApprocheCollection];
    }
    return alerteApprocheCollection;
  }

  protected convertDateFromClient<T extends IAlerteApproche | NewAlerteApproche | PartialUpdateAlerteApproche>(
    alerteApproche: T,
  ): RestOf<T> {
    return {
      ...alerteApproche,
      dateCreation: alerteApproche.dateCreation?.toJSON() ?? null,
      dateModification: alerteApproche.dateModification?.toJSON() ?? null,
      dernierDeclenchement: alerteApproche.dernierDeclenchement?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAlerteApproche: RestAlerteApproche): IAlerteApproche {
    return {
      ...restAlerteApproche,
      dateCreation: restAlerteApproche.dateCreation ? dayjs(restAlerteApproche.dateCreation) : undefined,
      dateModification: restAlerteApproche.dateModification ? dayjs(restAlerteApproche.dateModification) : undefined,
      dernierDeclenchement: restAlerteApproche.dernierDeclenchement ? dayjs(restAlerteApproche.dernierDeclenchement) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAlerteApproche>): HttpResponse<IAlerteApproche> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAlerteApproche[]>): HttpResponse<IAlerteApproche[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
