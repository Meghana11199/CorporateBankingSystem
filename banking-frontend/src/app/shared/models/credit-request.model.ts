export interface CreditRequest {
  id?: string;
  clientId: string;
  rmId?: string;

  requestedAmount: number;
  tenureMonths: number;
  purpose: string;

  status?: 'PENDING' | 'APPROVED' | 'REJECTED';
  createdAt?: string;
}
export interface CreateCreditRequestPayload {
  clientId: string;
  creditType: string;
  requestedAmount: number;
  tenureMonths: number;
  purpose: string;
}