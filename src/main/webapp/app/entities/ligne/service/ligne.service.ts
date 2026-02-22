import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILigne, NewLigne } from '../ligne.model';

export type PartialUpdateLigne = Partial<ILigne> & Pick<ILigne, 'id'>;

export type EntityResponseType = HttpResponse<ILigne>;
export type EntityArrayResponseType = HttpResponse<ILigne[]>;

@Injectable({ providedIn: 'root' })
export class LigneService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lignes');

  create(ligne: NewLigne): Observable<EntityResponseType> {
    return this.http.post<ILigne>(this.resourceUrl, ligne, { observe: 'response' });
  }

  update(ligne: ILigne): Observable<EntityResponseType> {
    return this.http.put<ILigne>(`${this.resourceUrl}/${this.getLigneIdentifier(ligne)}`, ligne, { observe: 'response' });
  }

  partialUpdate(ligne: PartialUpdateLigne): Observable<EntityResponseType> {
    return this.http.patch<ILigne>(`${this.resourceUrl}/${this.getLigneIdentifier(ligne)}`, ligne, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILigne>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILigne[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLigneIdentifier(ligne: Pick<ILigne, 'id'>): number {
    return ligne.id;
  }

  compareLigne(o1: Pick<ILigne, 'id'> | null, o2: Pick<ILigne, 'id'> | null): boolean {
    return o1 && o2 ? this.getLigneIdentifier(o1) === this.getLigneIdentifier(o2) : o1 === o2;
  }

  addLigneToCollectionIfMissing<Type extends Pick<ILigne, 'id'>>(
    ligneCollection: Type[],
    ...lignesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const lignes: Type[] = lignesToCheck.filter(isPresent);
    if (lignes.length > 0) {
      const ligneCollectionIdentifiers = ligneCollection.map(ligneItem => this.getLigneIdentifier(ligneItem));
      const lignesToAdd = lignes.filter(ligneItem => {
        const ligneIdentifier = this.getLigneIdentifier(ligneItem);
        if (ligneCollectionIdentifiers.includes(ligneIdentifier)) {
          return false;
        }
        ligneCollectionIdentifiers.push(ligneIdentifier);
        return true;
      });
      return [...lignesToAdd, ...ligneCollection];
    }
    return ligneCollection;
  }
}
