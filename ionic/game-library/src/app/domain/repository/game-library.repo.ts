/* eslint-disable max-len */
import { GameLibraryQuery } from '../../state/queries/game-library.query';
import { GameLibraryStore } from '../../state/game-library.store';
import { Injectable } from '@angular/core';
import { Currency, Format, Game, Platform } from '../model/game';
import { v4 as uuidv4 } from 'uuid';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
}
)
export class GameLibraryRepository {
  constructor(private gameStore: GameLibraryStore, private gameQueries: GameLibraryQuery) {
    this.gameStore.update(state => ({
      gamesList: [...this.initGameList()]
    }));
  }

  public addGameToList(game: Game): void {
    this.gameStore.update(state => ({
      gamesList: [...state.gamesList, {...game, id: uuidv4()}]
    }));
  }

  public updateGameFromList(gameToUpdate: Game): void {
    console.log(gameToUpdate);
    this.gameStore.update((state) => ({
      gamesList: [
        ...state.gamesList.map((game: Game) =>
          game.id === gameToUpdate.id ? { ...gameToUpdate } : game
        ),
      ],
    }));
  }

  public deleteGameFromList(gameId: string): void {
    this.gameStore.update((state) => ({
      gamesList: [...state.gamesList.filter((game: Game) => game.id !== gameId)]
    }));
  }

  public getGamesFromLibrary(): Observable<Game[]> {
    return this.gameQueries.selectAllGames();
  }

  public getGameByID(gameId: string): Observable<Game> {
    return this.gameQueries.selectGameById(gameId);
  }

  private initGameList() {
    return [
        {
          id: uuidv4(),
          name: 'Dark Souls: Remastered',
          producer: 'From Software',
          purchaseDate: new Date(2011, 4, 23),
          lastTimePlayed: new Date(2019, 6, 30, 16, 53),
          price: {priceValue: 32.99, currency: Currency.EUR},
          platform: Platform.PC,
          formatOfGame: Format.DIGITAL,
          picture: 'https://assets.nintendo.com/image/upload/c_fill,w_1200/q_auto:best/f_auto/dpr_2.0/ncom/en_US/games/switch/d/dark-souls-remastered-switch/hero'
        },
        {
          id: uuidv4(),
          name: 'Dark Souls II',
          producer: 'From Software',
          purchaseDate: new Date(2014, 1, 8),
          lastTimePlayed: new Date(2020, 9, 20, 12, 40),
          price: {priceValue: 25, currency: Currency.DOL},
          platform: Platform.XBOX,
          formatOfGame: Format.DISK,
          picture: 'https://store-images.s-microsoft.com/image/apps.2435.71415569152440938.6739ca29-cd37-4678-ab4c-9de7eea4d902.ebef4dc5-7000-4381-aeb3-ec706fb63c03?q=90&w=256&h=256&mode=crop&format=jpg&background=%230078D7'
        },
        {
          id: uuidv4(),
          name: 'Elden Ring',
          producer: 'From Software',
          purchaseDate: new Date(2022, 2, 12),
          lastTimePlayed: new Date(2022, 4, 13, 0, 20),
          price: {priceValue: 256.35, currency: Currency.RON},
          platform: Platform.PC,
          formatOfGame: Format.DIGITAL,
          picture: 'https://image.api.playstation.com/vulcan/ap/rnd/202108/0410/0Jz6uJLxOK7JOMMfcfHFBi1D.png'
        },
        {
          id: uuidv4(),
          name: 'God of War: Ragnarok',
          producer: 'SCE Santa Monica Studio',
          purchaseDate: new Date(2022, 9, 9),
          lastTimePlayed: new Date(2022, 10, 13, 1, 23),
          price: {priceValue: 50, currency: Currency.EUR},
          platform: Platform.PS,
          formatOfGame: Format.DISK,
          picture: 'https://image.api.playstation.com/vulcan/ap/rnd/202207/1210/4xJ8XB3bi888QTLZYdl7Oi0s.png'
        }

    ];
  }
}
