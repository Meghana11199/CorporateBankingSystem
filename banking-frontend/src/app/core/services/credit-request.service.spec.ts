import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';

import { CreditRequestService } from './credit-request.service';
import { environment } from '../../../evironments/environment';
import {
  CreateCreditRequestPayload,
  CreditRequest
} from '../../shared/models/credit-request.model';

describe('CreditRequestService', () => {
  let service: CreditRequestService;
  let httpMock: HttpTestingController;

  const rmBaseUrl = `${environment.apiUrl}/rm/credit-requests`;
  const analystBaseUrl = `${environment.apiUrl}/analyst/credit-requests`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(CreditRequestService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  /* ===================== RM ===================== */

  it('should create a credit request (RM)', () => {
    const payload: CreateCreditRequestPayload = {
      clientId: '1',
      amount: 500000,
      tenure: 12,
      purpose: 'Working Capital'
    } as unknown as CreateCreditRequestPayload;

    service.createCreditRequest(payload).subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne(rmBaseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    req.flush({});
  });

  it('should fetch RM credit requests', () => {
    const mockRequests: CreditRequest[] = [
      { id: '1', status: 'PENDING' } as CreditRequest
    ];

    service.getMyCreditRequests().subscribe(data => {
      expect(data.length).toBe(1);
    });

    const req = httpMock.expectOne(rmBaseUrl);
    expect(req.request.method).toBe('GET');

    req.flush(mockRequests);
  });

  it('should fetch all RM credit requests', () => {
    service.getAll().subscribe();

    const req = httpMock.expectOne(rmBaseUrl);
    expect(req.request.method).toBe('GET');

    req.flush([]);
  });

  /* ===================== ANALYST ===================== */

  it('should fetch pending credit requests', () => {
    const mockRequests: CreditRequest[] = [
      { id: '2', status: 'PENDING' } as CreditRequest
    ];

    service.getPendingRequests().subscribe(data => {
      expect(data[0].status).toBe('PENDING');
    });

    const req = httpMock.expectOne(`${analystBaseUrl}/pending`);
    expect(req.request.method).toBe('GET');

    req.flush(mockRequests);
  });

  it('should fetch credit request by id', () => {
    const mockRequest: CreditRequest = {
      id: '10',
      status: 'PENDING'
    } as CreditRequest;

    service.getCreditRequestById('10').subscribe(data => {
      expect(data.id).toBe('10');
    });

    const req = httpMock.expectOne(`${analystBaseUrl}/10`);
    expect(req.request.method).toBe('GET');

    req.flush(mockRequest);
  });

  it('should update credit request status', () => {
    const payload = {
      status: 'APPROVED' as const,
      analystComment: 'Looks good'
    };

    service.updateCreditRequest('5', payload).subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne(`${analystBaseUrl}/5`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(payload);

    req.flush({});
  });

  it('should fetch all credit requests for analyst', () => {
    service.getAllForAnalyst().subscribe();

    const req = httpMock.expectOne(analystBaseUrl);
    expect(req.request.method).toBe('GET');

    req.flush([]);
  });
});
