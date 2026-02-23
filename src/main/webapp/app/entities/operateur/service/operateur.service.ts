import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOperateur, NewOperateur } from '../operateur.model';

export type PartialUpdateOperateur = Partial<IOperateur> & Pick<IOperateur, 'id'>;

type RestOf<T extends IOperateur | NewOperateur> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

export type RestOperateur = RestOf<IOperateur>;

export type NewRestOperateur = RestOf<NewOperateur>;

export type PartialUpdateRestOperateur = RestOf<PartialUpdateOperateur>;

export type EntityResponseType = HttpResponse<IOperateur>;
export type EntityArrayResponseType = HttpResponse<IOperateur[]>;

@Injectable({ providedIn: 'root' })
export class OperateurService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operateurs');

  create(operateur: NewOperateur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operateur);
    return this.http
      .post<RestOperateur>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(operateur: IOperateur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operateur);
    return this.http
      .put<RestOperateur>(`${this.resourceUrl}/${this.getOperateurIdentifier(operateur)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(operateur: PartialUpdateOperateur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operateur);
    return this.http
      .patch<RestOperateur>(`${this.resourceUrl}/${this.getOperateurIdentifier(operateur)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOperateur>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOperateur[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOperateurIdentifier(operateur: Pick<IOperateur, 'id'>): number {
    return operateur.id;
  }

  compareOperateur(o1: Pick<IOperateur, 'id'> | null, o2: Pick<IOperateur, 'id'> | null): boolean {
    return o1 && o2 ? this.getOperateurIdentifier(o1) === this.getOperateurIdentifier(o2) : o1 === o2;
  }

  addOperateurToCollectionIfMissing<Type extends Pick<IOperateur, 'id'>>(
    operateurCollection: Type[],
    ...operateursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const operateurs: Type[] = operateursToCheck.filter(isPresent);
    if (operateurs.length > 0) {
      const operateurCollectionIdentifiers = operateurCollection.map(operateurItem => this.getOperateurIdentifier(operateurItem));
      const operateursToAdd = operateurs.filter(operateurItem => {
        const operateurIdentifier = this.getOperateurIdentifier(operateurItem);
        if (operateurCollectionIdentifiers.includes(operateurIdentifier)) {
          return false;
        }
        operateurCollectionIdentifiers.push(operateurIdentifier);
        return true;
      });
      return [...operateursToAdd, ...operateurCollection];
    }
    return operateurCollection;
  }

  protected convertDateFromClient<T extends IOperateur | NewOperateur | PartialUpdateOperateur>(operateur: T): RestOf<T> {
    return {
      ...operateur,
      dateCreation: operateur.dateCreation?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOperateur: RestOperateur): IOperateur {
    return {
      ...restOperateur,
      dateCreation: restOperateur.dateCreation ? dayjs(restOperateur.dateCreation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOperateur>): HttpResponse<IOperateur> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOperateur[]>): HttpResponse<IOperateur[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
