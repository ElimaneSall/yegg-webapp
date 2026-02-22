import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FavoriDetailComponent } from './favori-detail.component';

describe('Favori Management Detail Component', () => {
  let comp: FavoriDetailComponent;
  let fixture: ComponentFixture<FavoriDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FavoriDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./favori-detail.component').then(m => m.FavoriDetailComponent),
              resolve: { favori: () => of({ id: 20334 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FavoriDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FavoriDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load favori on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FavoriDetailComponent);

      // THEN
      expect(instance.favori()).toEqual(expect.objectContaining({ id: 20334 }));
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
