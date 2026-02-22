import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFavori, NewFavori } from '../favori.model';

export type PartialUpdateFavori = Partial<IFavori> & Pick<IFavori, 'id'>;

export type EntityResponseType = HttpResponse<IFavori>;
export type EntityArrayResponseType = HttpResponse<IFavori[]>;

@Injectable({ providedIn: 'root' })
export class FavoriService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/favoris');

  create(favori: NewFavori): Observable<EntityResponseType> {
    return this.http.post<IFavori>(this.resourceUrl, favori, { observe: 'response' });
  }

  update(favori: IFavori): Observable<EntityResponseType> {
    return this.http.put<IFavori>(`${this.resourceUrl}/${this.getFavoriIdentifier(favori)}`, favori, { observe: 'response' });
  }

  partialUpdate(favori: PartialUpdateFavori): Observable<EntityResponseType> {
    return this.http.patch<IFavori>(`${this.resourceUrl}/${this.getFavoriIdentifier(favori)}`, favori, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFavori>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFavori[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
