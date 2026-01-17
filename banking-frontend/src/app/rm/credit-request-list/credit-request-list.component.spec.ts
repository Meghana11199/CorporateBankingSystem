import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CreditRequestListComponent } from './credit-request-list.component';
import { CreditRequestService } from '../../core/services/credit-request.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { CreditRequest } from '../../shared/models/credit-request.model';
import { COMMON_TEST_IMPORTS, MOCK_ACTIVATED_ROUTE } from '../../../tests/test-imports';



describe('CreditRequestListComponent', () => {
  let component: CreditRequestListComponent;
  let fixture: ComponentFixture<CreditRequestListComponent>;
  let creditServiceSpy: jasmine.SpyObj<CreditRequestService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    creditServiceSpy = jasmine.createSpyObj('CreditRequestService', ['getAll']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    creditServiceSpy.getAll.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [
        CreditRequestListComponent,
        ...COMMON_TEST_IMPORTS
      ],
      providers: [
        { provide: CreditRequestService, useValue: creditServiceSpy },
        { provide: Router, useValue: routerSpy },
        MOCK_ACTIVATED_ROUTE
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CreditRequestListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load credit requests on init', () => {
    const mockData: CreditRequest[] = [
      {
        id: '1',
        clientId: 'C101',
        requestedAmount: 100000,
        tenureMonths: 12,
        purpose: 'Working Capital',
        status: 'PENDING'
      } as CreditRequest
    ];

    creditServiceSpy.getAll.and.returnValue(of(mockData));

    component.ngOnInit();

    expect(creditServiceSpy.getAll).toHaveBeenCalled();
    expect(component.creditRequests.data.length).toBe(1);
    expect(component.loading).toBeFalse();
  });

  it('should handle error when loading credit requests', () => {
    creditServiceSpy.getAll.and.returnValue(
      throwError(() => new Error('API Error'))
    );

    component.loadCreditRequests();

    expect(component.loading).toBeFalse();
  });

  it('should logout and navigate to login', () => {
    spyOn(localStorage, 'clear');

    component.logout();

    expect(localStorage.clear).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
