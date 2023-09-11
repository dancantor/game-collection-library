import { formatLastPlayedDistance } from './../../../../domain/helpers/date-time.helper';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Game } from 'src/app/domain/model/game';
import { Router } from '@angular/router';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-game-item',
  templateUrl: './game-item.component.html',
  styleUrls: ['./game-item.component.scss'],
})
export class GameItemComponent implements OnInit {
  @Input() currentGame: Game;
  @Output() deleteGameEmitter = new EventEmitter<string>();
  @Output() updateGameEmitter = new EventEmitter<string>();
  formatLastTimeWrapper = formatLastPlayedDistance;
  constructor(private router: Router, private alertController: AlertController) { }

  ngOnInit() {
  }

  onItemClick() {
    this.router.navigate(['game-details', `${this.currentGame.id}`]);
  }

  async onDeleteItem() {
    const deleteConfirmation = await this.alertController.create({
      header: 'Deleting Game',
      message: `Are you sure you want to delete ${this.currentGame.name}`,
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel'
        },
        {
          text: 'OK',
          role: 'confirm',
          handler: () => {
            this.deleteGameEmitter.emit(this.currentGame.id);
          },
        },
      ],
    });
    await deleteConfirmation.present();
  }

  onUpdateItem() {
    this.updateGameEmitter.emit(this.currentGame.id);
  }
}
