import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../evironments/environment';


interface LoginResponse {
  token: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private baseUrl = `${environment.apiUrl}/auth`;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  login(username: string, password: string) {
    return this.http.post<LoginResponse>(
      `${this.baseUrl}/login`,
      { username, password }
    );
  }

handleLoginSuccess(response: LoginResponse) {
  const normalizedRole = response.role.replace('ROLE_', '');

  localStorage.setItem('token', response.token);
  localStorage.setItem('role', normalizedRole);

  this.redirectByRole(normalizedRole);
}
  redirectByRole(role: string) {
    switch (role) {
      case 'RM':
        this.router.navigate(['/rm']);
        break;
      case 'ANALYST':
        this.router.navigate(['/analyst']);
        break;
      case 'ADMIN':
        this.router.navigate(['/admin']);
        break;
      default:
        this.router.navigate(['/login']);
    }
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}