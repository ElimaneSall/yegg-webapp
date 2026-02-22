import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILigneArret, NewLigneArret } from '../ligne-arret.model';

export type PartialUpdateLigneArret = Partial<ILigneArret> & Pick<ILigneArret, 'id'>;

export type EntityResponseType = HttpResponse<ILigneArret>;
export type EntityArrayResponseType = HttpResponse<ILigneArret[]>;

@Injectable({ providedIn: 'root' })
export class LigneArretService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ligne-arrets');

  create(ligneArret: NewLigneArret): Observable<EntityResponseType> {
    return this.http.post<ILigneArret>(this.resourceUrl, ligneArret, { observe: 'response' });
  }

  update(ligneArret: ILigneArret): Observable<EntityResponseType> {
    return this.http.put<ILigneArret>(`${this.resourceUrl}/${this.getLigneArretIdentifier(ligneArret)}`, ligneArret, {
      observe: 'response',
    });
  }

  partialUpdate(ligneArret: PartialUpdateLigneArret): Observable<EntityResponseType> {
    return this.http.patch<ILigneArret>(`${this.resourceUrl}/${this.getLigneArretIdentifier(ligneArret)}`, ligneArret, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILigneArret>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILigneArret[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLigneArretIdentifier(ligneArret: Pick<ILigneArret, 'id'>): number {
    return ligneArret.id;
  }

  compareLigneArret(o1: Pick<ILigneArret, 'id'> | null, o2: Pick<ILigneArret, 'id'> | null): boolean {
    return o1 && o2 ? this.getLigneArretIdentifier(o1) === this.getLigneArretIdentifier(o2) : o1 === o2;
  }

  addLigneArretToCollectionIfMissing<Type extends Pick<ILigneArret, 'id'>>(
    ligneArretCollection: Type[],
    ...ligneArretsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ligneArrets: Type[] = ligneArretsToCheck.filter(isPresent);
    if (ligneArrets.length > 0) {
      const ligneArretCollectionIdentifiers = ligneArretCollection.map(ligneArretItem => this.getLigneArretIdentifier(ligneArretItem));
      const ligneArretsToAdd = ligneArrets.filter(ligneArretItem => {
        const ligneArretIdentifier = this.getLigneArretIdentifier(ligneArretItem);
        if (ligneArretCollectionIdentifiers.includes(ligneArretIdentifier)) {
          return false;
        }
        ligneArretCollectionIdentifiers.push(ligneArretIdentifier);
        return true;
      });
      return [...ligneArretsToAdd, ...ligneArretCollection];
    }
    return ligneArretCollection;
  }
}
