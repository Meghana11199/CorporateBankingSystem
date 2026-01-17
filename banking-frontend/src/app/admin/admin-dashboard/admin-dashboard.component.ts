import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatCardModule
  ],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {

  constructor(private router: Router) {}

  goToCreateUser(): void {
    this.router.navigate(['/admin/create-user']);
  }

  goToManageUsers(): void {
    this.router.navigate(['/admin/users']);
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
