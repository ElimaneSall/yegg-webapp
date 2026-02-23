import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { HistoriqueAlerteDetailComponent } from './historique-alerte-detail.component';

describe('HistoriqueAlerte Management Detail Component', () => {
  let comp: HistoriqueAlerteDetailComponent;
  let fixture: ComponentFixture<HistoriqueAlerteDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistoriqueAlerteDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./historique-alerte-detail.component').then(m => m.HistoriqueAlerteDetailComponent),
              resolve: { historiqueAlerte: () => of({ id: 4252 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(HistoriqueAlerteDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoriqueAlerteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load historiqueAlerte on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', HistoriqueAlerteDetailComponent);

      // THEN
      expect(instance.historiqueAlerte()).toEqual(expect.objectContaining({ id: 4252 }));
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
