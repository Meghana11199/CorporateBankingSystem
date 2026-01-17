import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Router } from '@angular/router';
import { ClientService } from '../../core/services/client.service';

@Component({
  selector: 'app-create-client',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule
  ],
  templateUrl: './create-client.component.html',
  styleUrls: ['./create-client.component.css']
})
export class CreateClientComponent implements OnInit {

  clientForm!: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private clientService: ClientService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.clientForm = this.fb.group({
      companyName: ['', Validators.required],
      industry: ['', Validators.required],
      address: ['', Validators.required],
      primaryName: ['', Validators.required],
      primaryEmail: ['', [Validators.required, Validators.email]],
      primaryPhone: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
      annualTurnover: [null, [Validators.required, Validators.min(0.1)]],
      documentsSubmitted: [false, Validators.requiredTrue]
    });
  }

  onSubmit(): void {
    if (this.clientForm.invalid) return;

    this.clientService.createClient(this.clientForm.value).subscribe({
      next: () => {
        alert('Client created successfully');
        this.router.navigate(['/rm/clients']);
      },
      error: err => {
        console.error('Create client failed', err);
        alert('Failed to create client');
      }
    });
  }

  goBackToDashboard(): void {
    this.router.navigate(['/rm']);
  }

  cancel(): void {
    this.router.navigate(['/rm']);
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
