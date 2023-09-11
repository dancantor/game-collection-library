import { formatLastTimePlayed } from './../../domain/helpers/date-time.helper';
import { GameLibraryService } from './../../domain/service/game-library.service';
import { Format, Game } from 'src/app/domain/model/game';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { formatPurchaseDate } from 'src/app/domain/helpers/date-time.helper';

@Component({
  selector: 'app-game-details',
  templateUrl: './game-details.page.html',
  styleUrls: ['./game-details.page.scss'],
})
export class GameDetailsPage implements OnInit {
  selectedGame$: Observable<Game>;
  formatPurchaseDateWrapper = formatPurchaseDate;
  formatLastTimePlayedWrapper = formatLastTimePlayed;
  formatOfGameWrapper = Format;
  constructor(private activatedRoute: ActivatedRoute, private gameService: GameLibraryService, private location: Location) { }
  ngOnInit() {
    const gameId = this.activatedRoute.snapshot.paramMap.get('id');
    this.selectedGame$ = this.gameService.getGameById(gameId);
  }

  navigateBack(): void{
    this.location.back();
  }

}
