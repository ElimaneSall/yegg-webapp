import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlerteLigneArret, NewAlerteLigneArret } from '../alerte-ligne-arret.model';

export type PartialUpdateAlerteLigneArret = Partial<IAlerteLigneArret> & Pick<IAlerteLigneArret, 'id'>;

export type EntityResponseType = HttpResponse<IAlerteLigneArret>;
export type EntityArrayResponseType = HttpResponse<IAlerteLigneArret[]>;

@Injectable({ providedIn: 'root' })
export class AlerteLigneArretService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alerte-ligne-arrets');

  create(alerteLigneArret: NewAlerteLigneArret): Observable<EntityResponseType> {
    return this.http.post<IAlerteLigneArret>(this.resourceUrl, alerteLigneArret, { observe: 'response' });
  }

  update(alerteLigneArret: IAlerteLigneArret): Observable<EntityResponseType> {
    return this.http.put<IAlerteLigneArret>(
      `${this.resourceUrl}/${this.getAlerteLigneArretIdentifier(alerteLigneArret)}`,
      alerteLigneArret,
      { observe: 'response' },
    );
  }

  partialUpdate(alerteLigneArret: PartialUpdateAlerteLigneArret): Observable<EntityResponseType> {
    return this.http.patch<IAlerteLigneArret>(
      `${this.resourceUrl}/${this.getAlerteLigneArretIdentifier(alerteLigneArret)}`,
      alerteLigneArret,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAlerteLigneArret>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAlerteLigneArret[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAlerteLigneArretIdentifier(alerteLigneArret: Pick<IAlerteLigneArret, 'id'>): number {
    return alerteLigneArret.id;
  }

  compareAlerteLigneArret(o1: Pick<IAlerteLigneArret, 'id'> | null, o2: Pick<IAlerteLigneArret, 'id'> | null): boolean {
    return o1 && o2 ? this.getAlerteLigneArretIdentifier(o1) === this.getAlerteLigneArretIdentifier(o2) : o1 === o2;
  }

  addAlerteLigneArretToCollectionIfMissing<Type extends Pick<IAlerteLigneArret, 'id'>>(
    alerteLigneArretCollection: Type[],
    ...alerteLigneArretsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const alerteLigneArrets: Type[] = alerteLigneArretsToCheck.filter(isPresent);
    if (alerteLigneArrets.length > 0) {
      const alerteLigneArretCollectionIdentifiers = alerteLigneArretCollection.map(alerteLigneArretItem =>
        this.getAlerteLigneArretIdentifier(alerteLigneArretItem),
      );
      const alerteLigneArretsToAdd = alerteLigneArrets.filter(alerteLigneArretItem => {
        const alerteLigneArretIdentifier = this.getAlerteLigneArretIdentifier(alerteLigneArretItem);
        if (alerteLigneArretCollectionIdentifiers.includes(alerteLigneArretIdentifier)) {
          return false;
        }
        alerteLigneArretCollectionIdentifiers.push(alerteLigneArretIdentifier);
        return true;
      });
      return [...alerteLigneArretsToAdd, ...alerteLigneArretCollection];
    }
    return alerteLigneArretCollection;
  }
}
