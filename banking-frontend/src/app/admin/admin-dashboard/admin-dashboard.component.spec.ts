import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { AdminDashboardComponent } from './admin-dashboard.component';

describe('AdminDashboardComponent', () => {
  let component: AdminDashboardComponent;
  let fixture: ComponentFixture<AdminDashboardComponent>;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        AdminDashboardComponent, // ✅ standalone component
        RouterTestingModule.withRoutes([]) // ✅ mock router
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AdminDashboardComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should create the admin dashboard component', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to create user page', () => {
    spyOn(router, 'navigate');

    component.goToCreateUser();

    expect(router.navigate).toHaveBeenCalledWith(['/admin/create-user']);
  });

  it('should navigate to manage users page', () => {
    spyOn(router, 'navigate');

    component.goToManageUsers();

    expect(router.navigate).toHaveBeenCalledWith(['/admin/users']);
  });

  it('should clear localStorage and navigate to login on logout', () => {
    spyOn(localStorage, 'clear');
    spyOn(router, 'navigate');

    component.logout();

    expect(localStorage.clear).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});
