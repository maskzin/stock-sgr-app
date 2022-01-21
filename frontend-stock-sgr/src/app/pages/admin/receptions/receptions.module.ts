import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReceptionsRoutingModule } from './receptions-routing.module';
import { ReceptionsComponent } from './receptions.component';


import {MatTableModule} from '@angular/material/table';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { FormsModule } from '@angular/forms';
import {MatSelectModule} from '@angular/material/select';
import {MatDialogModule} from '@angular/material/dialog';
import { NgxLoadingModule,ngxLoadingAnimationTypes } from 'ngx-loading';




@NgModule({
  declarations: [
    ReceptionsComponent
  ],
  imports: [
    CommonModule,
    ReceptionsRoutingModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatTooltipModule,
    MatPaginatorModule,
    MatDatepickerModule,
    MatNativeDateModule,
    FormsModule,
    MatDialogModule,
    MatSelectModule,
    NgxLoadingModule.forRoot({
      animationType:ngxLoadingAnimationTypes.wanderingCubes
    })
  ]
})
export class ReceptionsModule { }
