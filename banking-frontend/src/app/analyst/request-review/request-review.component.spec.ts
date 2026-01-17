import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RequestReviewComponent } from './request-review.component';
import { CreditRequestService } from '../../core/services/credit-request.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { COMMON_TEST_IMPORTS, MOCK_ACTIVATED_ROUTE } from '../../../tests/test-imports';



describe('RequestReviewComponent', () => {
  let component: RequestReviewComponent;
  let fixture: ComponentFixture<RequestReviewComponent>;
  let creditService: jasmine.SpyObj<CreditRequestService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    creditService = jasmine.createSpyObj('CreditRequestService', [
      'getAllForAnalyst'
    ]);

    router = jasmine.createSpyObj('Router', ['navigate']);

    creditService.getAllForAnalyst.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [
        RequestReviewComponent,
        ...COMMON_TEST_IMPORTS
      ],
      providers: [
        { provide: CreditRequestService, useValue: creditService },
        { provide: Router, useValue: router },
        MOCK_ACTIVATED_ROUTE
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RequestReviewComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should load credit requests on init', () => {
    const mockData: any[] = [
      {
        clientId: 'C1',
        requestedAmount: 1000,
        tenureMonths: 12,
        status: 'PENDING',
        analystComment: ''
      }
    ];

    creditService.getAllForAnalyst.and.returnValue(of(mockData));

    fixture.detectChanges(); // triggers ngOnInit

    expect(creditService.getAllForAnalyst).toHaveBeenCalled();
    expect(component.creditRequests.length).toBe(1);
    expect(component.loading).toBeFalse();
  });

 it('should handle error while loading requests', () => {
  creditService.getAllForAnalyst.and.returnValue(
    throwError(() => new Error('API Error'))
  );

  fixture.detectChanges(); // triggers ngOnInit → loadAllRequests

  expect(component.loading).toBeFalse();
  expect(component.creditRequests.length).toBe(0);
});

  it('should logout and navigate to login', () => {
    component.logout();

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});
