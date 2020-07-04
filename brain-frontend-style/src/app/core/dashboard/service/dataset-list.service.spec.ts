import { TestBed } from '@angular/core/testing';

import { DataSetListService } from './dataset-list.service';

describe('DataSetListService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DataSetListService = TestBed.get(DataSetListService);
    expect(service).toBeTruthy();
  });
});
