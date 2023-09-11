import { Game } from './../domain/model/game';
import { Store, StoreConfig } from '@datorama/akita';
import { Injectable } from '@angular/core';

export interface GameLibraryState {
   gamesList: Game[];
}

export const createInitialState = (): GameLibraryState => ({gamesList: []});

@Injectable({
  providedIn: 'root'
})
@StoreConfig({ name: 'game-library' })
export class GameLibraryStore extends Store<GameLibraryState> {
  constructor() {
    super(createInitialState());
  }
}
