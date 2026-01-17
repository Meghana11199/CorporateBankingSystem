import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { AnalystDashboardComponent } from './analyst-dashboard.component';

describe('AnalystDashboardComponent', () => {
  let component: AnalystDashboardComponent;
  let fixture: ComponentFixture<AnalystDashboardComponent>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [AnalystDashboardComponent],
      providers: [
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AnalystDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to pending requests', () => {
    component.goToPending();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/analyst/pending-requests']);
  });

  it('should navigate to credit requests review', () => {
    component.goToReview();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/analyst/credit-requests']);
  });

  it('should clear localStorage and navigate to login on logout', () => {
    spyOn(localStorage, 'clear');

    component.logout();

    expect(localStorage.clear).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
