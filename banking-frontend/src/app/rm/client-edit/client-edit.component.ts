import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientService } from '../../core/services/client.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-client-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './client-edit.component.html',
  styleUrls: ['./client-edit.component.css']
})
export class ClientEditComponent implements OnInit {

  clientId!: string;
  form!: FormGroup;
  loading = true;
  errorMsg = '';
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private clientService: ClientService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    console.log('📌 CLIENT EDIT ID:', id);

    if (!id) {
      this.errorMsg = 'Invalid Client ID';
      this.loading = false;
      this.cdr.detectChanges();
      return;
    }

    this.clientId = id;

    this.form = this.fb.group({
      companyName: ['', Validators.required],
      industry: ['', Validators.required],
      address: ['', Validators.required],
      primaryName: ['', Validators.required],
      primaryEmail: ['', [Validators.required, Validators.email]],
      primaryPhone: ['', Validators.required],
      annualTurnover: ['', Validators.required],
      documentsSubmitted: [false]
    });

    this.loadClient();
  }

  loadClient(): void {
    console.log('📡 Fetching client:', this.clientId);

    this.clientService.getClientById(this.clientId).subscribe({
      next: client => {
        console.log('✅ CLIENT DATA:', client);
        this.form.patchValue(client);
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: err => {
        console.error('❌ API ERROR:', err);
        this.errorMsg = 'Failed to load client details';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  updateClient(): void {
    if (this.form.invalid || this.submitting) return;

    this.submitting = true;

    this.clientService.updateClient(this.clientId, this.form.value).subscribe({
      next: () => {
        this.submitting = false;

        this.snackBar.open(
          'Client details updated successfully',
          'OK',
          {
            duration: 3000,
            horizontalPosition: 'center',
            verticalPosition: 'top'
          }
        );

        setTimeout(() => {
          this.router.navigate(['/rm/clients']);
        }, 1000);
      },
      error: err => {
        console.error('❌ UPDATE ERROR:', err);
        this.submitting = false;

        this.snackBar.open(
          'Failed to update client',
          'Close',
          { duration: 3000 }
        );
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/rm/clients']);
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
