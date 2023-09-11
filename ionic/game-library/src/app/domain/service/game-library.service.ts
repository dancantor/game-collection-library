import { GameLibraryRepository } from './../repository/game-library.repo';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Game } from '../model/game';

@Injectable({
  providedIn: 'root'
})
export class GameLibraryService {

  constructor(private repo: GameLibraryRepository) { }

  public getAllGames(): Observable<Game[]> {
    return this.repo.getGamesFromLibrary();
  }

  public getGameById(gameId: string): Observable<Game> {
    return this.repo.getGameByID(gameId);
  }

  public addGame(game: Game): void {
    this.repo.addGameToList(game);
  }

  public deleteGame(gameId: string): void {
    this.repo.deleteGameFromList(gameId);
  }

  public updateGame(game: Game): void {
    this.repo.updateGameFromList(game);
  }
}
