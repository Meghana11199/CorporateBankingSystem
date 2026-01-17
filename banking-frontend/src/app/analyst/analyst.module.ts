import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AnalystRoutingModule } from './analyst-routing.module';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { CreditRequestListComponent } from '../rm/credit-request-list/credit-request-list.component';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    CreditRequestListComponent
  ]
})
export class AnalystModule { }
