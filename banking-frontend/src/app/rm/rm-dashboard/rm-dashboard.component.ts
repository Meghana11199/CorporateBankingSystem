import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { Router, RouterModule, RouterOutlet } from '@angular/router';


@Component({
  selector: 'app-rm-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    RouterOutlet,
    RouterModule
  ],
  templateUrl: './rm-dashboard.component.html',
  styleUrls: ['./rm-dashboard.component.css']
})
export class RmDashboardComponent {

  constructor(public router: Router) {}

  goClients(): void {
    this.router.navigate(['/rm/clients']);
  }

  goCreateClient(): void {
    this.router.navigate(['/rm/create-client']);
  }

  goCreateCredit(): void {
    this.router.navigate(['/rm/create-credit']);
  }

  goCreditList(): void {
    this.router.navigate(['/rm/credit-requests']);
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
