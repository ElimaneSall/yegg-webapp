import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AlerteLigneArretDetailComponent } from './alerte-ligne-arret-detail.component';

describe('AlerteLigneArret Management Detail Component', () => {
  let comp: AlerteLigneArretDetailComponent;
  let fixture: ComponentFixture<AlerteLigneArretDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlerteLigneArretDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./alerte-ligne-arret-detail.component').then(m => m.AlerteLigneArretDetailComponent),
              resolve: { alerteLigneArret: () => of({ id: 29289 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AlerteLigneArretDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AlerteLigneArretDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load alerteLigneArret on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AlerteLigneArretDetailComponent);

      // THEN
      expect(instance.alerteLigneArret()).toEqual(expect.objectContaining({ id: 29289 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
