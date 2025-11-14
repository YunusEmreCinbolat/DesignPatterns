import { TestBed } from '@angular/core/testing';

import { ApiProxyService } from './api-proxy.service';

describe('ApiProxyService', () => {
  let service: ApiProxyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApiProxyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
