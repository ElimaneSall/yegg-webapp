import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AlerteApprocheDetailComponent } from './alerte-approche-detail.component';

describe('AlerteApproche Management Detail Component', () => {
  let comp: AlerteApprocheDetailComponent;
  let fixture: ComponentFixture<AlerteApprocheDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlerteApprocheDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./alerte-approche-detail.component').then(m => m.AlerteApprocheDetailComponent),
              resolve: { alerteApproche: () => of({ id: 19175 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AlerteApprocheDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AlerteApprocheDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load alerteApproche on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AlerteApprocheDetailComponent);

      // THEN
      expect(instance.alerteApproche()).toEqual(expect.objectContaining({ id: 19175 }));
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
