import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientService } from '../../core/services/client.service';
import { Client } from '../../shared/models/client.model';

@Component({
  selector: 'app-client-details',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './client-details.component.html',
  styleUrls: ['./client-details.component.css']
})
export class ClientDetailsComponent implements OnInit {

  clientId!: string;
  client?: Client;
  loading = true;
  errorMsg = '';

  constructor(
    private route: ActivatedRoute,
    private clientService: ClientService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (!id) {
        this.errorMsg = 'Invalid client ID';
        this.loading = false;
        this.cdr.detectChanges();
        return;
      }

      this.clientId = id;
      this.loading = true;

      this.clientService.getClientById(id).subscribe({
        next: data => {
          console.log('Client API Response:', data);
          this.client = data;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: err => {
          console.error('Failed to load client', err);
          this.errorMsg = 'Failed to load client details';
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
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
