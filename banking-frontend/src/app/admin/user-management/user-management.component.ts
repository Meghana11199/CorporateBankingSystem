import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';

import { AdminService, UserDTO } from '../../core/services/admin.service';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatCardModule,
    MatListModule,
    MatIconModule
  ],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {

  users: UserDTO[] = [];
  loading = true;
  errorMessage = '';

  constructor(
    private adminService: AdminService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.errorMessage = '';

    this.adminService.getUsers().subscribe({
      next: (res) => {
        console.log('Users loaded:', res);
        this.users = res ?? [];
        this.loading = false;
        this.cdr.detectChanges(); // ensure UI update
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Failed to load users';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  toggleStatus(user: UserDTO): void {
    this.adminService.updateUserStatus(user.username, !user.active).subscribe({
      next: () => this.loadUsers(),
      error: () => {
        this.errorMessage = 'Failed to update user status';
        this.cdr.detectChanges();
      }
    });
  }
}
