import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { ClientService } from '../../core/services/client.service';
import { Router } from '@angular/router';
import { Client } from '../../shared/models/client.model';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css'],
})
export class ClientListComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = [
    'companyName',
    'primaryEmail',
    'primaryPhone',
    'annualTurnover',
    'actions'
  ];

  dataSource = new MatTableDataSource<Client>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private clientService: ClientService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadClients();

    this.dataSource.filterPredicate = (data: Client, filter: string) =>
      data.companyName?.toLowerCase().includes(filter) ||
      data.primaryEmail?.toLowerCase().includes(filter) ||
      data.primaryPhone?.toLowerCase().includes(filter);
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

  loadClients(): void {
    this.clientService.getClients().subscribe({
      next: clients => {
        console.log('CLIENTS FROM API:', clients);
        this.dataSource.data = clients;
      },
      error: err => console.error('Failed to load clients', err)
    });
  }

  applyFilter(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.dataSource.filter = value.trim().toLowerCase();
  }

  viewDetails(client: Client): void {
    this.router.navigate(['/rm/client', client.id]);
  }

  editClient(client: Client): void {
    this.router.navigate(['/rm/client', client.id, 'edit']);
  }

  goBackToDashboard(): void {
    this.router.navigate(['/rm']);
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
