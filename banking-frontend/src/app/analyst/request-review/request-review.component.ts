import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { Router, RouterModule } from '@angular/router';

import { CreditRequestService } from '../../core/services/credit-request.service';
import { CreditRequest } from '../../shared/models/credit-request.model';
import { AnalystHeaderComponent } from '../../shared/analyst-header/analyst-header.component';
import { AppFooterComponent } from '../../shared/app-footer/app-footer.component';

@Component({
  selector: 'app-request-review',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    RouterModule,
    AnalystHeaderComponent,
    AppFooterComponent
  ],
  templateUrl: './request-review.component.html',
  styleUrls: ['./request-review.component.css']
})
export class RequestReviewComponent implements OnInit {

  displayedColumns = [
    'clientId',
    'requestedAmount',
    'tenureMonths',
    'status',
    'analystComment'
  ];

  creditRequests: CreditRequest[] = [];
  loading = true;

  constructor(
    private creditService: CreditRequestService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadAllRequests();
  }

  loadAllRequests(): void {
    this.creditService.getAllForAnalyst().subscribe({
      next: data => {
        this.creditRequests = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
   backToDashboard(): void {
    this.router.navigate(['/analyst/dashboard']);
  }
}
