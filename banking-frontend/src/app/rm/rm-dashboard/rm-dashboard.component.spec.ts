import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { RmDashboardComponent } from './rm-dashboard.component';

describe('RmDashboardComponent', () => {
  let component: RmDashboardComponent;
  let fixture: ComponentFixture<RmDashboardComponent>;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RmDashboardComponent,              // ✅ standalone component
        RouterTestingModule.withRoutes([]) // ✅ mock router
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RmDashboardComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should create the RM dashboard component', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to clients list', () => {
    spyOn(router, 'navigate');

    component.goClients();

    expect(router.navigate).toHaveBeenCalledWith(['/rm/clients']);
  });

  it('should navigate to create client page', () => {
    spyOn(router, 'navigate');

    component.goCreateClient();

    expect(router.navigate).toHaveBeenCalledWith(['/rm/create-client']);
  });

  it('should navigate to create credit page', () => {
    spyOn(router, 'navigate');

    component.goCreateCredit();

    expect(router.navigate).toHaveBeenCalledWith(['/rm/create-credit']);
  });

  it('should navigate to credit requests list', () => {
    spyOn(router, 'navigate');

    component.goCreditList();

    expect(router.navigate).toHaveBeenCalledWith(['/rm/credit-requests']);
  });

  it('should clear localStorage and navigate to login on logout', () => {
    spyOn(localStorage, 'clear');
    spyOn(router, 'navigate');

    component.logout();

    expect(localStorage.clear).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});
