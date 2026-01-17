import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../evironments/environment';


export interface UserDTO {
  username: string;
  email: string;
  password?: string;     // 🔴 password should NOT be required on fetch
  role: 'RM' | 'ANALYST' | 'ADMIN';
  active: boolean;
}

@Injectable({ providedIn: 'root' })
export class AdminService {

  private readonly baseUrl = `${environment.apiUrl}/admin/users`;

  constructor(private http: HttpClient) {}

  /** Get all users */
  getUsers(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(this.baseUrl);
  }

  /** Create new user */
  createUser(user: UserDTO): Observable<UserDTO> {
    return this.http.post<UserDTO>(this.baseUrl, {
      ...user,
      active: user.active ?? true
    });
  }

  /** Activate / Deactivate user */
  updateUserStatus(username: string, active: boolean): Observable<UserDTO> {
    const params = new HttpParams().set('active', String(active));

    return this.http.patch<UserDTO>(
      `${this.baseUrl}/${username}/status`,
      {},
      { params }
    );
  }
}
