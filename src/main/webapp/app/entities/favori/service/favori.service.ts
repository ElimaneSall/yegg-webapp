import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFavori, NewFavori } from '../favori.model';

export type PartialUpdateFavori = Partial<IFavori> & Pick<IFavori, 'id'>;

type RestOf<T extends IFavori | NewFavori> = Omit<T, 'dateAjout' | 'dernierAcces'> & {
  dateAjout?: string | null;
  dernierAcces?: string | null;
};

export type RestFavori = RestOf<IFavori>;

export type NewRestFavori = RestOf<NewFavori>;

export type PartialUpdateRestFavori = RestOf<PartialUpdateFavori>;

export type EntityResponseType = HttpResponse<IFavori>;
export type EntityArrayResponseType = HttpResponse<IFavori[]>;

@Injectable({ providedIn: 'root' })
export class FavoriService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/favoris');

  create(favori: NewFavori): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(favori);
    return this.http
      .post<RestFavori>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(favori: IFavori): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(favori);
    return this.http
      .put<RestFavori>(`${this.resourceUrl}/${this.getFavoriIdentifier(favori)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(favori: PartialUpdateFavori): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(favori);
    return this.http
      .patch<RestFavori>(`${this.resourceUrl}/${this.getFavoriIdentifier(favori)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFavori>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFavori[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFavoriIdentifier(favori: Pick<IFavori, 'id'>): number {
    return favori.id;
  }

  compareFavori(o1: Pick<IFavori, 'id'> | null, o2: Pick<IFavori, 'id'> | null): boolean {
    return o1 && o2 ? this.getFavoriIdentifier(o1) === this.getFavoriIdentifier(o2) : o1 === o2;
  }

  addFavoriToCollectionIfMissing<Type extends Pick<IFavori, 'id'>>(
    favoriCollection: Type[],
    ...favorisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const favoris: Type[] = favorisToCheck.filter(isPresent);
    if (favoris.length > 0) {
      const favoriCollectionIdentifiers = favoriCollection.map(favoriItem => this.getFavoriIdentifier(favoriItem));
      const favorisToAdd = favoris.filter(favoriItem => {
        const favoriIdentifier = this.getFavoriIdentifier(favoriItem);
        if (favoriCollectionIdentifiers.includes(favoriIdentifier)) {
          return false;
        }
        favoriCollectionIdentifiers.push(favoriIdentifier);
        return true;
      });
      return [...favorisToAdd, ...favoriCollection];
    }
    return favoriCollection;
  }

  protected convertDateFromClient<T extends IFavori | NewFavori | PartialUpdateFavori>(favori: T): RestOf<T> {
    return {
      ...favori,
      dateAjout: favori.dateAjout?.toJSON() ?? null,
      dernierAcces: favori.dernierAcces?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFavori: RestFavori): IFavori {
    return {
      ...restFavori,
      dateAjout: restFavori.dateAjout ? dayjs(restFavori.dateAjout) : undefined,
      dernierAcces: restFavori.dernierAcces ? dayjs(restFavori.dernierAcces) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFavori>): HttpResponse<IFavori> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFavori[]>): HttpResponse<IFavori[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
