import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReviewCreditComponent } from './review-credit.component';
import { CreditRequestService } from '../../core/services/credit-request.service';
import { of, throwError } from 'rxjs';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { COMMON_TEST_IMPORTS } from '../../../tests/test-imports';

describe('ReviewCreditComponent', () => {
  let component: ReviewCreditComponent;
  let fixture: ComponentFixture<ReviewCreditComponent>;
  let creditServiceSpy: jasmine.SpyObj<CreditRequestService>;

  beforeEach(async () => {
    creditServiceSpy = jasmine.createSpyObj('CreditRequestService', [
      'getCreditRequestById',
      'updateCreditRequest'
    ]);

    creditServiceSpy.getCreditRequestById.and.returnValue(
      of({ id: '123', status: 'PENDING' } as any)
    );

    creditServiceSpy.updateCreditRequest.and.returnValue(of({}));

    await TestBed.configureTestingModule({
      imports: [
        ReviewCreditComponent,
        ...COMMON_TEST_IMPORTS
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of(convertToParamMap({ id: '123' })),
            snapshot: {
              paramMap: convertToParamMap({ id: '123' })
            }
          }
        },
        { provide: CreditRequestService, useValue: creditServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewCreditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // ngOnInit
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load credit request on init', () => {
    expect(creditServiceSpy.getCreditRequestById).toHaveBeenCalledWith('123');
    expect(component.loading).toBeFalse();
  });

  it('should show snackbar if comment is empty', () => {
    spyOn(component['snackBar'], 'open');
    component.submit('APPROVED');
    expect(component['snackBar'].open).toHaveBeenCalled();
  });

  it('should submit approval successfully', () => {
    component.request = { id: '123' } as any;
    component.analystComment = 'ok';

    component.submit('APPROVED');

    expect(creditServiceSpy.updateCreditRequest).toHaveBeenCalled();
  });

  it('should handle submit error', () => {
    creditServiceSpy.updateCreditRequest.and.returnValue(
      throwError(() => new Error('fail'))
    );

    component.request = { id: '123' } as any;
    component.analystComment = 'test';

    component.submit('REJECTED');

    expect(component.submitting).toBeFalse();
  });
});
