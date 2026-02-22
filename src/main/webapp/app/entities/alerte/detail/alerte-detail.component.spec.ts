import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AlerteDetailComponent } from './alerte-detail.component';

describe('Alerte Management Detail Component', () => {
  let comp: AlerteDetailComponent;
  let fixture: ComponentFixture<AlerteDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlerteDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./alerte-detail.component').then(m => m.AlerteDetailComponent),
              resolve: { alerte: () => of({ id: 7756 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AlerteDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AlerteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load alerte on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AlerteDetailComponent);

      // THEN
      expect(instance.alerte()).toEqual(expect.objectContaining({ id: 7756 }));
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
