import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { RouterModule, Router } from '@angular/router';

import { Client } from '../../shared/models/client.model';
import { ClientService } from '../../core/services/client.service';
import { CreditRequestService } from '../../core/services/credit-request.service';
import { CreateCreditRequestPayload } from '../../shared/models/credit-request.model';

@Component({
  selector: 'app-create-credit-request',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    RouterModule
  ],
  templateUrl: './create-credit-request.component.html',
  styleUrls: ['./create-credit-request.component.css']
})
export class CreateCreditRequestComponent implements OnInit {

  creditForm!: FormGroup;
  clients: Client[] = [];
  loading = false;
  successMsg = '';
  errorMsg = '';

  creditTypes = [
    'Term Loan',
    'Working Capital',
    'Financial Loan',
    'Business Loan'
  ];

  constructor(
    private fb: FormBuilder,
    private clientService: ClientService,
    private creditService: CreditRequestService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.creditForm = this.fb.group({
      clientId: ['', Validators.required],
      creditType: ['', Validators.required],
      requestedAmount: [null, [Validators.required, Validators.min(1)]],
      tenureMonths: [null, [Validators.required, Validators.min(1)]],
      purpose: ['', Validators.required]
    });

    this.loadClients();
  }

  loadClients(): void {
    this.clientService.getClients().subscribe({
      next: clients => this.clients = clients,
      error: err => console.error(err)
    });
  }

  submit(): void {
    if (this.creditForm.invalid) return;

    const payload: CreateCreditRequestPayload = this.creditForm.value;

    this.loading = true;

    this.creditService.createCreditRequest(payload).subscribe({
      next: () => {
        this.successMsg = 'Credit request created successfully!';
        this.creditForm.reset();
        this.loading = false;
      },
      error: err => {
        console.error('API ERROR:', err);
        this.errorMsg = 'Failed to create credit request';
        this.loading = false;
      }
    });
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
