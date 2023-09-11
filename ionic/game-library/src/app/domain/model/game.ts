/* eslint-disable @typescript-eslint/naming-convention */
export interface Game {
  id: string;
  name: string;
  producer: string;
  purchaseDate: Date;
  lastTimePlayed: Date;
  price: Price;
  platform: Platform;
  formatOfGame: Format;
  picture: string;
}

export interface Price {
  priceValue: number;
  currency: Currency;
}

export enum Currency {
  EUR = '€',
  DOL = '$',
  RON = 'RON'
}

export enum Platform {
  PC = 'PC',
  PS = 'PS',
  XBOX = 'XBOX'
}

export enum Format {
  DISK = 'DISK',
  DIGITAL = 'DIGITAL'
}
