import { CommonModule, NgIfContext } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, TemplateRef } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';

import { CreditRequest } from '../../shared/models/credit-request.model';
import { CreditRequestService } from '../../core/services/credit-request.service';
import { AnalystHeaderComponent } from '../../shared/analyst-header/analyst-header.component';
import { AppFooterComponent } from '../../shared/app-footer/app-footer.component';

@Component({
  selector: 'app-pending-requests',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    RouterModule,
    AnalystHeaderComponent,
    AppFooterComponent
  ],
  templateUrl: './pending-requests.component.html',
  styleUrls: ['./pending-requests.component.css']
})
export class PendingRequestsComponent implements OnInit {

  displayedColumns = [
    'clientId',
    'requestedAmount',
    'tenureMonths',
    'purpose',
    'actions'
  ];

  dataSource = new MatTableDataSource<CreditRequest>([]);
  loading = true;
  errorMsg = '';
emptyTpl: TemplateRef<NgIfContext<number>> | null | undefined;

  constructor(
    private creditService: CreditRequestService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadPendingRequests();
  }

  loadPendingRequests(): void {
    this.loading = true;

    this.creditService.getPendingRequests().subscribe({
      next: data => {
        this.dataSource.data = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMsg = 'Failed to load pending credit requests';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  review(id: string): void {
    this.router.navigate(['/analyst/review', id]);
  }

  backToDashboard(): void {
    this.router.navigate(['/analyst/dashboard']);
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
