import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PendingRequestsComponent } from './pending-requests.component';
import { CreditRequestService } from '../../core/services/credit-request.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { COMMON_TEST_IMPORTS } from '../../../tests/test-imports';



describe('PendingRequestsComponent', () => {
  let component: PendingRequestsComponent;
  let fixture: ComponentFixture<PendingRequestsComponent>;
  let creditServiceSpy: jasmine.SpyObj<CreditRequestService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    creditServiceSpy = jasmine.createSpyObj('CreditRequestService', [
      'getPendingRequests'
    ]);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    creditServiceSpy.getPendingRequests.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [
        PendingRequestsComponent,
        ...COMMON_TEST_IMPORTS
      ],
      providers: [
        { provide: CreditRequestService, useValue: creditServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PendingRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // triggers ngOnInit
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load pending requests on init', () => {
    const mockData: any[] = [
      {
        id: '1',
        clientId: 'C100',
        requestedAmount: 50000,
        tenureMonths: 12,
        purpose: 'Working Capital'
      }
    ];

    creditServiceSpy.getPendingRequests.and.returnValue(of(mockData));

    component.loadPendingRequests();

    expect(creditServiceSpy.getPendingRequests).toHaveBeenCalled();
    expect(component.dataSource.data.length).toBe(1);
    expect(component.loading).toBeFalse();
  });

  it('should handle error when loading pending requests fails', () => {
    creditServiceSpy.getPendingRequests.and.returnValue(
      throwError(() => new Error('API Error'))
    );

    component.loadPendingRequests();

    expect(component.errorMsg).toBe('Failed to load pending credit requests');
    expect(component.loading).toBeFalse();
  });

  it('should navigate to review page', () => {
    component.review('123');

    expect(routerSpy.navigate).toHaveBeenCalledWith([
      '/analyst/review',
      '123'
    ]);
  });

  it('should navigate back to dashboard', () => {
    component.backToDashboard();

    expect(routerSpy.navigate).toHaveBeenCalledWith([
      '/analyst/dashboard'
    ]);
  });

  it('should logout and navigate to login', () => {
    spyOn(localStorage, 'clear');

    component.logout();

    expect(localStorage.clear).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
