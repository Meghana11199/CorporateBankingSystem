import { TestBed } from '@angular/core/testing';
import {
  HttpHandler,
  HttpRequest,
  HttpEvent,
  HttpResponse
} from '@angular/common/http';
import { of } from 'rxjs';
import { JwtInterceptor } from './jwt-interceptor';

describe('JwtInterceptor', () => {
  let interceptor: JwtInterceptor;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [JwtInterceptor]
    });

    interceptor = TestBed.inject(JwtInterceptor);
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('should add Authorization header when token exists', () => {
    localStorage.setItem('token', 'test-token');

    const request = new HttpRequest('GET', '/api/test');

    const next: HttpHandler = {
      handle: (req: HttpRequest<any>) => {
        expect(req.headers.has('Authorization')).toBeTrue();
        expect(req.headers.get('Authorization')).toBe('Bearer test-token');

        return of(new HttpResponse({ status: 200 }));
      }
    };

    interceptor.intercept(request, next).subscribe();
  });

  it('should NOT add Authorization header when token does not exist', () => {
    const request = new HttpRequest('GET', '/api/test');

    const next: HttpHandler = {
      handle: (req: HttpRequest<any>) => {
        expect(req.headers.has('Authorization')).toBeFalse();

        return of(new HttpResponse({ status: 200 }));
      }
    };

    interceptor.intercept(request, next).subscribe();
  });
});
