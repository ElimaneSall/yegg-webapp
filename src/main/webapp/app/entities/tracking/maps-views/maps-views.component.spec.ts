import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MapsViewsComponent } from './maps-views.component';

describe('MapsViewsComponent', () => {
  let component: MapsViewsComponent;
  let fixture: ComponentFixture<MapsViewsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MapsViewsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(MapsViewsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
