import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

/**
 * Common imports for ALL standalone component tests
 */
export const COMMON_TEST_IMPORTS = [
  HttpClientTestingModule,
  RouterTestingModule,
  NoopAnimationsModule
];

/**
 * Default mock for ActivatedRoute (paramMap.subscribe SAFE)
 */
export const MOCK_ACTIVATED_ROUTE = {
  provide: ActivatedRoute,
  useValue: {
    paramMap: of(convertToParamMap({ id: '123' })),
    queryParams: of({})
  }
};
