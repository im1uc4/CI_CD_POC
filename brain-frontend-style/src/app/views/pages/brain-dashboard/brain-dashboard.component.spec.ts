import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BrainDashboardComponent } from './brain-dashboard.component';

describe('BrainDashboardComponent', () => {
  let component: BrainDashboardComponent;
  let fixture: ComponentFixture<BrainDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BrainDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BrainDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
