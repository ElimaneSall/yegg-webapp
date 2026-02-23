import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHistoriqueAlerte, NewHistoriqueAlerte } from '../historique-alerte.model';

export type PartialUpdateHistoriqueAlerte = Partial<IHistoriqueAlerte> & Pick<IHistoriqueAlerte, 'id'>;

type RestOf<T extends IHistoriqueAlerte | NewHistoriqueAlerte> = Omit<T, 'dateDeclenchement' | 'dateLecture'> & {
  dateDeclenchement?: string | null;
  dateLecture?: string | null;
};

export type RestHistoriqueAlerte = RestOf<IHistoriqueAlerte>;

export type NewRestHistoriqueAlerte = RestOf<NewHistoriqueAlerte>;

export type PartialUpdateRestHistoriqueAlerte = RestOf<PartialUpdateHistoriqueAlerte>;

export type EntityResponseType = HttpResponse<IHistoriqueAlerte>;
export type EntityArrayResponseType = HttpResponse<IHistoriqueAlerte[]>;

@Injectable({ providedIn: 'root' })
export class HistoriqueAlerteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/historique-alertes');

  create(historiqueAlerte: NewHistoriqueAlerte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historiqueAlerte);
    return this.http
      .post<RestHistoriqueAlerte>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(historiqueAlerte: IHistoriqueAlerte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historiqueAlerte);
    return this.http
      .put<RestHistoriqueAlerte>(`${this.resourceUrl}/${this.getHistoriqueAlerteIdentifier(historiqueAlerte)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(historiqueAlerte: PartialUpdateHistoriqueAlerte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historiqueAlerte);
    return this.http
      .patch<RestHistoriqueAlerte>(`${this.resourceUrl}/${this.getHistoriqueAlerteIdentifier(historiqueAlerte)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHistoriqueAlerte>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHistoriqueAlerte[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHistoriqueAlerteIdentifier(historiqueAlerte: Pick<IHistoriqueAlerte, 'id'>): number {
    return historiqueAlerte.id;
  }

  compareHistoriqueAlerte(o1: Pick<IHistoriqueAlerte, 'id'> | null, o2: Pick<IHistoriqueAlerte, 'id'> | null): boolean {
    return o1 && o2 ? this.getHistoriqueAlerteIdentifier(o1) === this.getHistoriqueAlerteIdentifier(o2) : o1 === o2;
  }

  addHistoriqueAlerteToCollectionIfMissing<Type extends Pick<IHistoriqueAlerte, 'id'>>(
    historiqueAlerteCollection: Type[],
    ...historiqueAlertesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const historiqueAlertes: Type[] = historiqueAlertesToCheck.filter(isPresent);
    if (historiqueAlertes.length > 0) {
      const historiqueAlerteCollectionIdentifiers = historiqueAlerteCollection.map(historiqueAlerteItem =>
        this.getHistoriqueAlerteIdentifier(historiqueAlerteItem),
      );
      const historiqueAlertesToAdd = historiqueAlertes.filter(historiqueAlerteItem => {
        const historiqueAlerteIdentifier = this.getHistoriqueAlerteIdentifier(historiqueAlerteItem);
        if (historiqueAlerteCollectionIdentifiers.includes(historiqueAlerteIdentifier)) {
          return false;
        }
        historiqueAlerteCollectionIdentifiers.push(historiqueAlerteIdentifier);
        return true;
      });
      return [...historiqueAlertesToAdd, ...historiqueAlerteCollection];
    }
    return historiqueAlerteCollection;
  }

  protected convertDateFromClient<T extends IHistoriqueAlerte | NewHistoriqueAlerte | PartialUpdateHistoriqueAlerte>(
    historiqueAlerte: T,
  ): RestOf<T> {
    return {
      ...historiqueAlerte,
      dateDeclenchement: historiqueAlerte.dateDeclenchement?.toJSON() ?? null,
      dateLecture: historiqueAlerte.dateLecture?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHistoriqueAlerte: RestHistoriqueAlerte): IHistoriqueAlerte {
    return {
      ...restHistoriqueAlerte,
      dateDeclenchement: restHistoriqueAlerte.dateDeclenchement ? dayjs(restHistoriqueAlerte.dateDeclenchement) : undefined,
      dateLecture: restHistoriqueAlerte.dateLecture ? dayjs(restHistoriqueAlerte.dateLecture) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHistoriqueAlerte>): HttpResponse<IHistoriqueAlerte> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHistoriqueAlerte[]>): HttpResponse<IHistoriqueAlerte[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
