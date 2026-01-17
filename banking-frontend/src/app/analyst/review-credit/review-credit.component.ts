import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';

import { CreditRequestService } from '../../core/services/credit-request.service';
import { CreditRequest } from '../../shared/models/credit-request.model';

@Component({
  selector: 'app-review-credit',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatInputModule,
    MatSnackBarModule
  ],
  templateUrl: './review-credit.component.html',
  styleUrls: ['./review-credit.component.css']
})
export class ReviewCreditComponent implements OnInit {

  request!: CreditRequest;
  analystComment = '';
  loading = true;
  submitting = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private creditService: CreditRequestService,
    private cdr: ChangeDetectorRef,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.loadRequest(id);
  }

  loadRequest(id: string): void {
    this.creditService.getCreditRequestById(id).subscribe({
      next: data => {
        this.request = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: err => {
        console.error('❌ Failed to load request', err);
        this.router.navigate(['/analyst/pending-requests']);
      }
    });
  }

  submit(status: 'APPROVED' | 'REJECTED'): void {

    if (!this.analystComment.trim()) {
      this.snackBar.open('Please add analyst comment', 'Close', {
        duration: 3000
      });
      return;
    }

    this.submitting = true;

    this.creditService.updateCreditRequest(this.request.id!, {
      status,
      analystComment: this.analystComment
    }).subscribe({
      next: () => {
        this.submitting = false;

        this.snackBar.open(
          `Your response has been saved (${status})`,
          'OK',
          { duration: 3000 }
        );

        setTimeout(() => {
          this.router.navigate(['/analyst/pending-requests']);
        }, 1000);
      },
      error: () => {
        this.submitting = false;
        this.snackBar.open('Failed to save response', 'Close', {
          duration: 3000
        });
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/analyst/pending-requests']);
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
