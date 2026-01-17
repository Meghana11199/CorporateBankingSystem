export interface ClientRequest {
  companyName: string;
  primaryEmail: string;
  primaryPhone: string;
  annualTurnover: number;
  industry?: string;
  address?: string;
  primaryName?: string;
  documentsSubmitted?: boolean;
}