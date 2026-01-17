import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../core/services/auth.service';
import { of, throwError } from 'rxjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', [
      'login',
      'handleLoginSuccess'
    ]);

    authServiceSpy.login.and.returnValue(
      of({ token: 'fake-token', role: 'ADMIN' })
    );

    await TestBed.configureTestingModule({
      imports: [
        LoginComponent,
        NoopAnimationsModule // ✅ fixes Angular Material animation error
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not submit if form is invalid', () => {
    component.submit();
    expect(authServiceSpy.login).not.toHaveBeenCalled();
  });

  it('should login successfully and call handleLoginSuccess', () => {
    component.loginForm.setValue({
      username: 'admin',
      password: 'password'
    });

    component.submit();

    expect(authServiceSpy.login).toHaveBeenCalledWith('admin', 'password');
    expect(authServiceSpy.handleLoginSuccess).toHaveBeenCalled();
    expect(component.loading).toBeFalse();
  });

  it('should show error on login failure', () => {
    authServiceSpy.login.and.returnValue(
      throwError(() => new Error('Invalid credentials'))
    );

    component.loginForm.setValue({
      username: 'wrong',
      password: 'wrong'
    });

    component.submit();

    expect(component.error).toBe('Invalid username or password');
    expect(component.loading).toBeFalse();
  });
});
