import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { IonicModule } from '@ionic/angular';
import {MatFormFieldModule} from '@angular/material/form-field';
import { AddUpdateGamePageRoutingModule } from './add-update-game-routing.module';

import { AddUpdateGamePage } from './add-update-game.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    AddUpdateGamePageRoutingModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule
  ],
  declarations: [AddUpdateGamePage]
})
export class AddUpdateGamePageModule {}
