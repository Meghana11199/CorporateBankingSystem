import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientListComponent } from './client-list/client-list.component';
import { CreateClientComponent } from './create-client/create-client.component';
import { ClientDetailsComponent } from './client-details/client-details.component';
import { ClientEditComponent } from './client-edit/client-edit.component';
import { CreateCreditRequestComponent } from './create-credit-request/create-credit-request.component';
import { CreditRequestListComponent } from './credit-request-list/credit-request-list.component';
import { RmDashboardComponent } from './rm-dashboard/rm-dashboard.component';


export const RM_ROUTES: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

  { path: 'dashboard', component: RmDashboardComponent },
  { path: 'clients', component: ClientListComponent },
  { path: 'create-client', component: CreateClientComponent },

  { path: 'client/:id', component: ClientDetailsComponent },
  { path: 'client/:id/edit', component: ClientEditComponent },

  { path: 'create-credit', component: CreateCreditRequestComponent },
  { path: 'credit-requests', component: CreditRequestListComponent }
];

@NgModule({
  imports: [RouterModule.forChild(RM_ROUTES)],
  exports: [RouterModule]
})
export class RmRoutingModule {}
