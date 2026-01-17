import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CreateUserComponent } from './create-user.component';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { AdminService } from '../../core/services/admin.service';
import { of, throwError } from 'rxjs';

describe('CreateUserComponent', () => {
  let component: CreateUserComponent;
  let fixture: ComponentFixture<CreateUserComponent>;
  let adminServiceSpy: jasmine.SpyObj<AdminService>;
  let router: Router;

  beforeEach(async () => {
    adminServiceSpy = jasmine.createSpyObj('AdminService', ['createUser']);

    await TestBed.configureTestingModule({
      imports: [
        CreateUserComponent,
        ReactiveFormsModule,
        RouterTestingModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: AdminService, useValue: adminServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CreateUserComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);

    spyOn(router, 'navigate');

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values', () => {
    expect(component.userForm.value).toEqual({
      username: '',
      email: '',
      password: '',
      role: ''
    });
  });

  it('should mark form invalid when empty', () => {
    expect(component.userForm.invalid).toBeTrue();
  });

  it('should not submit when form is invalid', () => {
    component.submit();
    expect(adminServiceSpy.createUser).not.toHaveBeenCalled();
  });

  it('should call adminService.createUser on valid submit', () => {
    component.userForm.setValue({
      username: 'admin1',
      email: 'admin@test.com',
      password: 'password123',
      role: 'ADMIN'
    });
adminServiceSpy.createUser.and.returnValue(
  of({
    username: 'admin1',
    email: 'admin@test.com',
    role: 'ADMIN',
    active: true
  })
);

    component.submit();

    expect(adminServiceSpy.createUser).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/admin']);
  });

  it('should show error alert when create user fails', () => {
    spyOn(window, 'alert');

    component.userForm.setValue({
      username: 'admin1',
      email: 'admin@test.com',
      password: 'password123',
      role: 'ADMIN'
    });

    adminServiceSpy.createUser.and.returnValue(
      throwError(() => new Error('Create failed'))
    );

    component.submit();

    expect(window.alert).toHaveBeenCalledWith('Failed to create employee');
  });

  it('should navigate back to admin dashboard', () => {
    component.goBack();
    expect(router.navigate).toHaveBeenCalledWith(['/admin']);
  });

  it('should clear localStorage and navigate to login on logout', () => {
    spyOn(localStorage, 'clear');

    component.logout();

    expect(localStorage.clear).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});
