export interface Client {
name: any;
email: any;
  id: string;                 // or number, depending on backend
  companyName: string;
  industry: string;
  address: string;
  primaryName: string;
  primaryEmail: string;
  primaryPhone: string;
  annualTurnover: number;
  documentsSubmitted: boolean;
}