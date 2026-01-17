import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { roleGuardGuard } from './role-guard.guard';
import { AuthService } from '../services/auth.service';

describe('roleGuardGuard', () => {
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  const executeGuard = (route: ActivatedRouteSnapshot) =>
    TestBed.runInInjectionContext(() =>
      roleGuardGuard(route, {} as any)
    );

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['getRole']);
    routerSpy = jasmine.createSpyObj<Router>('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });
  });

  it('should allow access when role is allowed', () => {
    authServiceSpy.getRole.and.returnValue('ADMIN');

    const route = {
      data: { roles: ['ADMIN', 'MANAGER'] }
    } as unknown as ActivatedRouteSnapshot;

    const result = executeGuard(route);

    expect(result).toBeTrue();
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });

  it('should redirect to login when role is not allowed', () => {
    authServiceSpy.getRole.and.returnValue('USER');

    const route = {
      data: { roles: ['ADMIN'] }
    } as unknown as ActivatedRouteSnapshot;

    const result = executeGuard(route);

    expect(result).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should redirect to login when user has no role', () => {
    authServiceSpy.getRole.and.returnValue(null);

    const route = {
      data: { roles: ['ADMIN'] }
    } as unknown as ActivatedRouteSnapshot;

    const result = executeGuard(route);

    expect(result).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
