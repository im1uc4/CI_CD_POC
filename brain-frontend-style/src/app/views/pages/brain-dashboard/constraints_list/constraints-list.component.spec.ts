import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConstraintsListComponent } from './constraints-list.component';

describe('ConstraintsListComponent', () => {
  let component: ConstraintsListComponent;
  let fixture: ComponentFixture<ConstraintsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConstraintsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConstraintsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
