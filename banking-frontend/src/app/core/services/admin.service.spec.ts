import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';

import { AdminService, UserDTO } from './admin.service';
import { environment } from '../../../evironments/environment';

describe('AdminService', () => {
  let service: AdminService;
  let httpMock: HttpTestingController;

  const baseUrl = `${environment.apiUrl}/admin/users`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(AdminService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all users', () => {
    const mockUsers: UserDTO[] = [
      {
        username: 'admin1',
        email: 'admin@test.com',
        role: 'ADMIN',
        active: true
      }
    ];

    service.getUsers().subscribe(users => {
      expect(users.length).toBe(1);
      expect(users[0].username).toBe('admin1');
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockUsers);
  });

  it('should create a new user', () => {
    const newUser: UserDTO = {
      username: 'analyst1',
      email: 'analyst@test.com',
      password: 'password123',
      role: 'ANALYST',
      active: true
    };

    service.createUser(newUser).subscribe(user => {
      expect(user.username).toBe('analyst1');
      expect(user.role).toBe('ANALYST');
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body.active).toBeTrue();
    req.flush(newUser);
  });

  it('should update user active status', () => {
    const updatedUser: UserDTO = {
      username: 'rm1',
      email: 'rm@test.com',
      role: 'RM',
      active: false
    };

    service.updateUserStatus('rm1', false).subscribe(user => {
      expect(user.active).toBeFalse();
    });

    const req = httpMock.expectOne(req =>
      req.method === 'PATCH' &&
      req.url === `${baseUrl}/rm1/status` &&
      req.params.get('active') === 'false'
    );

    req.flush(updatedUser);
  });
});
