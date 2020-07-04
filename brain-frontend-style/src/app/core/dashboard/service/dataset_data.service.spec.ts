import { TestBed } from '@angular/core/testing';
import { DataSetDataService } from './dataset_data.service';

describe('DataSetDataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DataSetDataService = TestBed.get(DataSetDataService);
    expect(service).toBeTruthy();
  });
});
