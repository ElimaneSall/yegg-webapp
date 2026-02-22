import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IArret, NewArret } from '../arret.model';

export type PartialUpdateArret = Partial<IArret> & Pick<IArret, 'id'>;

export type EntityResponseType = HttpResponse<IArret>;
export type EntityArrayResponseType = HttpResponse<IArret[]>;

@Injectable({ providedIn: 'root' })
export class ArretService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/arrets');

  create(arret: NewArret): Observable<EntityResponseType> {
    return this.http.post<IArret>(this.resourceUrl, arret, { observe: 'response' });
  }

  update(arret: IArret): Observable<EntityResponseType> {
    return this.http.put<IArret>(`${this.resourceUrl}/${this.getArretIdentifier(arret)}`, arret, { observe: 'response' });
  }

  partialUpdate(arret: PartialUpdateArret): Observable<EntityResponseType> {
    return this.http.patch<IArret>(`${this.resourceUrl}/${this.getArretIdentifier(arret)}`, arret, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IArret>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IArret[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getArretIdentifier(arret: Pick<IArret, 'id'>): number {
    return arret.id;
  }

  compareArret(o1: Pick<IArret, 'id'> | null, o2: Pick<IArret, 'id'> | null): boolean {
    return o1 && o2 ? this.getArretIdentifier(o1) === this.getArretIdentifier(o2) : o1 === o2;
  }

  addArretToCollectionIfMissing<Type extends Pick<IArret, 'id'>>(
    arretCollection: Type[],
    ...arretsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const arrets: Type[] = arretsToCheck.filter(isPresent);
    if (arrets.length > 0) {
      const arretCollectionIdentifiers = arretCollection.map(arretItem => this.getArretIdentifier(arretItem));
      const arretsToAdd = arrets.filter(arretItem => {
        const arretIdentifier = this.getArretIdentifier(arretItem);
        if (arretCollectionIdentifiers.includes(arretIdentifier)) {
          return false;
        }
        arretCollectionIdentifiers.push(arretIdentifier);
        return true;
      });
      return [...arretsToAdd, ...arretCollection];
    }
    return arretCollection;
  }
}
