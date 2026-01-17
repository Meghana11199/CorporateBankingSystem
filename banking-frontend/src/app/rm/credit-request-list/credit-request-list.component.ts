import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterModule, Router } from '@angular/router';

import { CreditRequest } from '../../shared/models/credit-request.model';
import { CreditRequestService } from '../../core/services/credit-request.service';

@Component({
  selector: 'app-credit-request-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    RouterModule
  ],
  templateUrl: './credit-request-list.component.html',
  styleUrls: ['./credit-request-list.component.css']
})
export class CreditRequestListComponent implements OnInit {

  displayedColumns = [
    'id',
    'clientId',
    'requestedAmount',
    'tenureMonths',
    'purpose',
    'status'
  ];

  creditRequests = new MatTableDataSource<CreditRequest>([]);
  loading = true;

  constructor(
    private creditService: CreditRequestService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCreditRequests();
  }

  loadCreditRequests(): void {
    this.creditService.getAll().subscribe({
      next: data => {
        console.log('DATA:', data);
        this.creditRequests.data = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: err => {
        console.error('Failed to load credit requests', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
