import { inject } from '@angular/core';
import { CanActivateFn, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuardGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot
) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const allowedRoles = route.data['roles'] as string[];
  const userRole = authService.getRole();

  if (userRole && allowedRoles.includes(userRole)) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};
