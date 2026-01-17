import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CreateCreditRequestComponent } from './create-credit-request.component';
import { ClientService } from '../../core/services/client.service';
import { CreditRequestService } from '../../core/services/credit-request.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { COMMON_TEST_IMPORTS, MOCK_ACTIVATED_ROUTE } from '../../../tests/test-imports';

describe('CreateCreditRequestComponent', () => {
  let component: CreateCreditRequestComponent;
  let fixture: ComponentFixture<CreateCreditRequestComponent>;
  let clientServiceSpy: jasmine.SpyObj<ClientService>;
  let creditServiceSpy: jasmine.SpyObj<CreditRequestService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    clientServiceSpy = jasmine.createSpyObj('ClientService', ['getClients']);
    creditServiceSpy = jasmine.createSpyObj('CreditRequestService', [
      'createCreditRequest'
    ]);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    clientServiceSpy.getClients.and.returnValue(of([]));
    creditServiceSpy.createCreditRequest.and.returnValue(of({}));

    await TestBed.configureTestingModule({
      imports: [
        CreateCreditRequestComponent,
        ...COMMON_TEST_IMPORTS
      ],
      providers: [
        { provide: ClientService, useValue: clientServiceSpy },
        { provide: CreditRequestService, useValue: creditServiceSpy },
        { provide: Router, useValue: routerSpy },
        MOCK_ACTIVATED_ROUTE
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CreateCreditRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // ngOnInit
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load clients on init', () => {
    expect(clientServiceSpy.getClients).toHaveBeenCalled();
  });

  it('should submit credit request when form is valid', () => {
    component.creditForm.setValue({
      clientId: 'C1',
      creditType: 'Term Loan',
      requestedAmount: 100000,
      tenureMonths: 12,
      purpose: 'Business expansion'
    });

    component.submit();

    expect(creditServiceSpy.createCreditRequest).toHaveBeenCalled();
    expect(component.successMsg).toBeTruthy();
    expect(component.loading).toBeFalse();
  });

  it('should handle error when credit request creation fails', () => {
    spyOn(console, 'error'); // ✅ REQUIRED FIX

    creditServiceSpy.createCreditRequest.and.returnValue(
      throwError(() => new Error('API Error'))
    );

    component.creditForm.setValue({
      clientId: 'C1',
      creditType: 'Term Loan',
      requestedAmount: 100000,
      tenureMonths: 12,
      purpose: 'Business expansion'
    });

    component.submit();

    expect(component.errorMsg).toBe('Failed to create credit request');
    expect(component.loading).toBeFalse();
    expect(console.error).toHaveBeenCalled(); // ✅ confirms error path
  });

  it('should logout and navigate to login', () => {
    spyOn(localStorage, 'clear');

    component.logout();

    expect(localStorage.clear).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
