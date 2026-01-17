import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


import { Observable } from 'rxjs';
import { environment } from '../../../evironments/environment';
import { CreateCreditRequestPayload, CreditRequest } from '../../shared/models/credit-request.model';


@Injectable({ providedIn: 'root' })
export class CreditRequestService {

  // RM endpoints
  private rmBaseUrl = `${environment.apiUrl}/rm/credit-requests`;

  // Analyst endpoints
  private analystBaseUrl = `${environment.apiUrl}/analyst/credit-requests`;

  constructor(private http: HttpClient) {}

  /* ===================== RM ===================== */

createCreditRequest(payload: CreateCreditRequestPayload) {
    return this.http.post(this.rmBaseUrl, payload);
  }

  getMyCreditRequests(): Observable<CreditRequest[]> {
    return this.http.get<CreditRequest[]>(this.rmBaseUrl);
  }

   getAll(): Observable<CreditRequest[]> {
    return this.http.get<CreditRequest[]>(this.rmBaseUrl);
  }
  /* ===================== ANALYST ===================== */

getPendingRequests(): Observable<CreditRequest[]> {
  return this.http.get<CreditRequest[]>(
    `${this.analystBaseUrl}/pending`
  );
}

// updateRequest(
//     id: string,
//     payload: { status: 'APPROVED' | 'REJECTED'; analystComment: string }
//   ) {
//     return this.http.put(
//       `${this.analystBaseUrl}/${id}`,
//       payload
//     );
//   }
  getCreditRequestById(id: string): Observable<CreditRequest> {
  return this.http.get<CreditRequest>(
    `${this.analystBaseUrl}/${id}`
  );
}
updateCreditRequest(id: string, payload: {
  status: 'APPROVED' | 'REJECTED';
  analystComment: string;
}) {
  return this.http.put(
    `${this.analystBaseUrl}/${id}`,
    payload
  );
}
getAllForAnalyst(): Observable<CreditRequest[]> {
  return this.http.get<CreditRequest[]>(
    `${this.analystBaseUrl}`
  );
}
  //  approveRequest(id: string) {
  //   return this.http.patch(
  //     `${this.analystBaseUrl}/${id}/approve`,
  //     {}
  //   );
  // }

  // rejectRequest(id: string) {
  //   return this.http.patch(
  //     `${this.analystBaseUrl}/${id}/reject`,
  //     {}
  //   );
  // }
 
  
}

