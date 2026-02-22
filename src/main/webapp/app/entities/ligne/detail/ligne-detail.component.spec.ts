import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { LigneDetailComponent } from './ligne-detail.component';

describe('Ligne Management Detail Component', () => {
  let comp: LigneDetailComponent;
  let fixture: ComponentFixture<LigneDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LigneDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./ligne-detail.component').then(m => m.LigneDetailComponent),
              resolve: { ligne: () => of({ id: 18806 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LigneDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LigneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load ligne on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LigneDetailComponent);

      // THEN
      expect(instance.ligne()).toEqual(expect.objectContaining({ id: 18806 }));
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
