import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DishgenDetailComponent } from './dishgen-detail.component';

describe('DishgenDetailComponent', () => {
  let component: DishgenDetailComponent;
  let fixture: ComponentFixture<DishgenDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DishgenDetailComponent]
    });
    fixture = TestBed.createComponent(DishgenDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
