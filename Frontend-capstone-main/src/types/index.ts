// User types
export interface User {
  id: string;
  username: string;
  email: string;
  role: 'ADMIN' | 'RM' | 'ANALYST';
  active: boolean;
}

export interface AuthResponse {
  token: string;
  username: string;
  email: string;
  role: string;
  userId: string;
}

// Client types
export interface PrimaryContact {
  name: string;
  email: string;
  phone: string;
}

export interface Client {
  id: string;
  companyName: string;
  industry: string;
  address: string;
  primaryContact: PrimaryContact;
  annualTurnover: number;
  documentsSubmitted: boolean;
  rmId: string;
}

// Credit Request types
export interface CreditRequest {
  id: string;
  clientId: string;
  submittedBy: string;
  requestAmount: number;
  tenureMonths: number;
  purpose: string;
  status: 'Pending' | 'Approved' | 'Rejected';
  remarks: string;
  createdAt: string;
}