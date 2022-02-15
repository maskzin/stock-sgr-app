import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EpavesRoutingModule } from './epaves-routing.module';
import { EpavesComponent } from './epaves.component';


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
import { FilterPipeModule } from 'ngx-filter-pipe';
import { registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import {MatRadioModule} from '@angular/material/radio';
import {MatTabsModule} from '@angular/material/tabs';



registerLocaleData(localeFr, 'fr');




@NgModule({
  declarations: [
    EpavesComponent
  ],
  imports: [
    CommonModule,
    EpavesRoutingModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatTooltipModule,
    MatPaginatorModule,
    MatDatepickerModule,
    MatNativeDateModule,
    FormsModule,
    PdfViewerModule,
    MatRadioModule,
    MatTabsModule,
    MatDialogModule,
    MatSelectModule,
    FilterPipeModule,
    NgxLoadingModule.forRoot({
      animationType:ngxLoadingAnimationTypes.wanderingCubes
    })
  ]
})
export class EpavesModule { }
