jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AlerteLigneArretService } from '../service/alerte-ligne-arret.service';

import { AlerteLigneArretDeleteDialogComponent } from './alerte-ligne-arret-delete-dialog.component';

describe('AlerteLigneArret Management Delete Component', () => {
  let comp: AlerteLigneArretDeleteDialogComponent;
  let fixture: ComponentFixture<AlerteLigneArretDeleteDialogComponent>;
  let service: AlerteLigneArretService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AlerteLigneArretDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(AlerteLigneArretDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AlerteLigneArretDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AlerteLigneArretService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
