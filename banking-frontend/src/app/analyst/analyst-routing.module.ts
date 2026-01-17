import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

export const ANALYST_ROUTES: Routes = [

  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },

  {
    path: 'dashboard',
    loadComponent: () =>
      import('./analyst-dashboard/analyst-dashboard.component')
        .then(m => m.AnalystDashboardComponent)
  },

  {
    path: 'pending-requests',
    loadComponent: () =>
      import('./pending-requests/pending-requests.component')
        .then(m => m.PendingRequestsComponent)
  },

  {
    path: 'credit-requests',
    loadComponent: () =>
      import('./request-review/request-review.component')
        .then(m => m.RequestReviewComponent)
  },

  {
    path: 'review/:id',
    loadComponent: () =>
      import('./review-credit/review-credit.component')
        .then(m => m.ReviewCreditComponent)
  }

];

@NgModule({
  imports: [RouterModule.forChild(ANALYST_ROUTES)],
  exports: [RouterModule]
})
export class AnalystRoutingModule {}
