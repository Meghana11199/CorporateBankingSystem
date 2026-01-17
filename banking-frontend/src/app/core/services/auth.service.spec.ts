import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { Router } from '@angular/router';

import { AuthService } from './auth.service';
import { environment } from '../../../evironments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let routerSpy: jasmine.SpyObj<Router>;

  const baseUrl = `${environment.apiUrl}/auth`;

  beforeEach(() => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [{ provide: Router, useValue: routerSpy }]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);

    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // ============================
  // login()
  // ============================
  it('should call login API', () => {
    const mockResponse = {
      token: 'jwt-token',
      role: 'ROLE_ADMIN'
    };

    service.login('admin', 'password').subscribe(res => {
      expect(res.token).toBe('jwt-token');
      expect(res.role).toBe('ROLE_ADMIN');
    });

    const req = httpMock.expectOne(`${baseUrl}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      username: 'admin',
      password: 'password'
    });

    req.flush(mockResponse);
  });

  // ============================
  // handleLoginSuccess()
  // ============================
  it('should store token & role and redirect ADMIN', () => {
    service.handleLoginSuccess({
      token: 'jwt-token',
      role: 'ROLE_ADMIN'
    });

    expect(localStorage.getItem('token')).toBe('jwt-token');
    expect(localStorage.getItem('role')).toBe('ADMIN');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/admin']);
  });

  // ============================
  // redirectByRole()
  // ============================
  it('should redirect RM', () => {
    service.redirectByRole('RM');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/rm']);
  });

  it('should redirect ANALYST', () => {
    service.redirectByRole('ANALYST');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/analyst']);
  });

  it('should redirect to login for unknown role', () => {
    service.redirectByRole('UNKNOWN');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });

  // ============================
  // isLoggedIn()
  // ============================
  it('should return true if token exists', () => {
    localStorage.setItem('token', 'jwt');
    expect(service.isLoggedIn()).toBeTrue();
  });

  it('should return false if token does not exist', () => {
    expect(service.isLoggedIn()).toBeFalse();
  });

  // ============================
  // getRole()
  // ============================
  it('should return role from localStorage', () => {
    localStorage.setItem('role', 'ADMIN');
    expect(service.getRole()).toBe('ADMIN');
  });

  // ============================
  // logout()
  // ============================
  it('should clear localStorage and navigate to login on logout', () => {
    localStorage.setItem('token', 'jwt');
    localStorage.setItem('role', 'ADMIN');

    service.logout();

    expect(localStorage.getItem('token')).toBeNull();
    expect(localStorage.getItem('role')).toBeNull();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
