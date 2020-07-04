import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SaveDatasetDialogComponent } from './save-dataset-dialog.component';

describe('SaveDatasetDialogComponent', () => {
  let component: SaveDatasetDialogComponent;
  let fixture: ComponentFixture<SaveDatasetDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SaveDatasetDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SaveDatasetDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
