import { TestBed } from '@angular/core/testing';

import { BrainDashboard_Service } from './brain-dashboard.service';

describe('BrainDashboard_Service', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service:BrainDashboard_Service = TestBed.get(BrainDashboard_Service);
    expect(service).toBeTruthy();
  });
});
