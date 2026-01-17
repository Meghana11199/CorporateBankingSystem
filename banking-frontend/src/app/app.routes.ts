import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { authGuardGuard } from './core/guards/auth-guard.guard';
import { roleGuardGuard } from './core/guards/role-guard.guard';
import { UnauthorizedComponent } from './shared/unauthorized/unauthorized.component';


export const routes: Routes = [
  { path: 'login', component: LoginComponent},

  {
    path: 'rm',
    canActivate: [authGuardGuard, roleGuardGuard],
    data: { roles: ['RM'] },
    loadChildren: () =>
      import('./rm/rm-routing.module').then(m => m.RM_ROUTES)
  },

  {
    path: 'analyst',
    canActivate: [authGuardGuard, roleGuardGuard],
    data: { roles: ['ANALYST'] },
    loadChildren: () =>
      import('./analyst/analyst-routing.module').then(m => m.ANALYST_ROUTES)
  },

  {
    path: 'admin',
    canActivate: [authGuardGuard, roleGuardGuard],
    data: { roles: ['ADMIN'] },
    loadChildren: () =>
      import('./admin/admin-routing.module').then(m => m.ADMIN_ROUTES)
  },

  { path: 'unauthorized', component: UnauthorizedComponent },

  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];
