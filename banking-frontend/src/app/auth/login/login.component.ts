import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
     CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
loginForm: FormGroup;
  loading = false;
  error = '';
credentials: any;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService
  ) {
    // Initialize the reactive form
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  submit() {
    if (this.loginForm.invalid) return;

    this.loading = true;
    this.error = '';

    const { username, password } = this.loginForm.value;

    // Call AuthService login and delegate success handling
    this.authService.login(username, password).subscribe({
      next: (res) => {
        this.authService.handleLoginSuccess(res); // ✅ token, role, redirect
        this.loading = false;
      },
      error: () => {
        this.error = 'Invalid username or password';
        this.loading = false;
      }
    });
  }
}
