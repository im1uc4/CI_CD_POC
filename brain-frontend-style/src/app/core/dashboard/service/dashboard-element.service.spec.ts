import { TestBed } from '@angular/core/testing';

import { DashboardElement_Service } from './dashboard-element.service';

describe('DashboardElement.ServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DashboardElement_Service = TestBed.get(DashboardElement_Service);
    expect(service).toBeTruthy();
  });
});
