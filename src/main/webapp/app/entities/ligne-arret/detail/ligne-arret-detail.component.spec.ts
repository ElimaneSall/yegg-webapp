import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { LigneArretDetailComponent } from './ligne-arret-detail.component';

describe('LigneArret Management Detail Component', () => {
  let comp: LigneArretDetailComponent;
  let fixture: ComponentFixture<LigneArretDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LigneArretDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./ligne-arret-detail.component').then(m => m.LigneArretDetailComponent),
              resolve: { ligneArret: () => of({ id: 24214 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LigneArretDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LigneArretDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load ligneArret on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LigneArretDetailComponent);

      // THEN
      expect(instance.ligneArret()).toEqual(expect.objectContaining({ id: 24214 }));
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
