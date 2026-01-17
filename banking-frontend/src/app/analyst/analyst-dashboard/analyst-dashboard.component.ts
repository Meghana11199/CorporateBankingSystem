import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { AppFooterComponent } from '../../shared/app-footer/app-footer.component';
import { AnalystHeaderComponent } from '../../shared/analyst-header/analyst-header.component';

@Component({
  selector: 'app-analyst-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    AnalystHeaderComponent,
    AppFooterComponent
  ],
  templateUrl: './analyst-dashboard.component.html',
  styleUrls: ['./analyst-dashboard.component.css']
})
export class AnalystDashboardComponent {

  constructor(private router: Router) {}

  goToPending(): void {
    this.router.navigate(['/analyst/pending-requests']);
  }

  goToReview(): void {
    this.router.navigate(['/analyst/credit-requests']);
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
