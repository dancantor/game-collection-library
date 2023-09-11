import { GameLibraryService } from './../../domain/service/game-library.service';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Game } from 'src/app/domain/model/game';
import { Router } from '@angular/router';

@Component({
  selector: 'app-game-library-list',
  templateUrl: './game-library-list.page.html',
  styleUrls: ['./game-library-list.page.scss'],
})
export class GameLibraryListPage implements OnInit {
  gamesList$: Observable<Game[]>;

  constructor(private gamesService: GameLibraryService, private router: Router) { }

  ngOnInit() {
    this.gamesList$ = this.gamesService.getAllGames();
  }

  onDeleteGame(gameId: string): void {
    this.gamesService.deleteGame(gameId);
  }

  onUpdateGame(gameId: string): void {
    this.router.navigate(['add-update-game'], {queryParams: {gameId}});
  }

  navigateToAddPage() {
    this.router.navigate(['add-update-game']);
  }
}
