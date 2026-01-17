import { TestBed } from '@angular/core/testing';
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { CanActivateFn } from '@angular/router';
import { authGuardGuard } from './auth-guard.guard';
import { AuthService } from '../services/auth.service';

describe('authGuardGuard', () => {
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  const mockRoute = {} as ActivatedRouteSnapshot;
  const mockState = {} as RouterStateSnapshot;

  const executeGuard: CanActivateFn = (route, state) =>
    TestBed.runInInjectionContext(() => authGuardGuard(route, state));

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['isLoggedIn']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });
  });

  it('should allow activation when user is logged in', () => {
    authServiceSpy.isLoggedIn.and.returnValue(true);

    const result = executeGuard(mockRoute, mockState);

    expect(result).toBeTrue();
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });

  it('should redirect to login when user is not logged in', () => {
    authServiceSpy.isLoggedIn.and.returnValue(false);

    const result = executeGuard(mockRoute, mockState);

    expect(result).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
