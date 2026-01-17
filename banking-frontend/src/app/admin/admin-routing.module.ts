import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { roleGuardGuard } from '../core/guards/role-guard.guard';
import { routes } from '../app.routes';

export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./admin-dashboard/admin-dashboard.component')
        .then(m => m.AdminDashboardComponent),
    canActivate: [roleGuardGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'create-user',
    loadComponent: () =>
      import('./create-user/create-user.component')
        .then(m => m.CreateUserComponent),
    canActivate: [roleGuardGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'users',
    loadComponent: () =>
      import('./user-management/user-management.component')
        .then(m => m.UserManagementComponent),
    canActivate: [roleGuardGuard],
    data: { roles: ['ADMIN'] }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
