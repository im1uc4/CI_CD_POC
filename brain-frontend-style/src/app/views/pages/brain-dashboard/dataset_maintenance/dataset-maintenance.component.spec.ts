import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DataSetMaintenanceComponent } from './dataset-maintenance.component';

describe('DatasetMaintenanceComponent', () => {
  let component: DataSetMaintenanceComponent;
  let fixture: ComponentFixture<DataSetMaintenanceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DataSetMaintenanceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataSetMaintenanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
