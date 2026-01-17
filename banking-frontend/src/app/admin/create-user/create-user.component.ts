import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { AdminService } from '../../core/services/admin.service';

@Component({
  selector: 'app-create-user',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent {

  userForm: FormGroup;
  roles: string[] = ['ADMIN', 'ANALYST', 'RM'];

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private router: Router
  ) {
    this.userForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: ['', Validators.required]
    });
  }

  submit(): void {
    if (this.userForm.invalid) return;

    this.adminService.createUser(this.userForm.value).subscribe({
      next: () => {
        alert('Employee created successfully');
        this.router.navigate(['/admin']);
      },
      error: () => alert('Failed to create employee')
    });
  }

  goBack(): void {
    this.router.navigate(['/admin']);
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
