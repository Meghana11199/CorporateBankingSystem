import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserManagementComponent } from './user-management.component';
import { AdminService } from '../../core/services/admin.service';

describe('UserManagementComponent', () => {
  let component: UserManagementComponent;
  let fixture: ComponentFixture<UserManagementComponent>;
  let adminServiceSpy: jasmine.SpyObj<AdminService>;

  beforeEach(async () => {
    adminServiceSpy = jasmine.createSpyObj('AdminService', [
      'getUsers',
      'updateUserStatus'
    ]);

    adminServiceSpy.getUsers.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [UserManagementComponent],
      providers: [
        { provide: AdminService, useValue: adminServiceSpy },

        // ✅ REQUIRED because RouterModule is used
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { paramMap: { get: () => null } }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(UserManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load users on init', () => {
    expect(adminServiceSpy.getUsers).toHaveBeenCalled();
  });
});
