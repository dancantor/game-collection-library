/* eslint-disable @typescript-eslint/member-ordering */
import { GameLibraryState, GameLibraryStore } from './../game-library.store';
import { Query } from '@datorama/akita';
import { Game } from 'src/app/domain/model/game';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GameLibraryQuery extends Query<GameLibraryState> {
  constructor(protected store: GameLibraryStore) {
    super(store);
  }

  private allGames$ = this.select((state: GameLibraryState) => state.gamesList);
  private gameById$ = (gameId: string) =>
    this.select((state: GameLibraryState) =>
      state.gamesList.find((game: Game) => game.id === gameId)
    );

  public selectAllGames = (): Observable<Game[]> => this.allGames$;
  public selectGameById = (gameId: string): Observable<Game> => this.gameById$(gameId);
}
