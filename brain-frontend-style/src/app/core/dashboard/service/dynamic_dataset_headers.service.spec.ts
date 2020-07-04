import { TestBed } from '@angular/core/testing';

import { DynamicHeaderService } from './dynamic_dataset_headers.service';

describe('DynamicHeaderService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DynamicHeaderService = TestBed.get(DynamicHeaderService);
    expect(service).toBeTruthy();
  });
});
