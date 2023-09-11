import { Currency, Format, Platform, Game } from './../../domain/model/game';
import { parseLastTimePlayedString, parsePurchaseDateString, formatPurchaseDateForm, formatLastTimePlayedForm } from './../../domain/helpers/date-time.helper';
/* eslint-disable @typescript-eslint/member-ordering */
import { formatLastTimePlayed } from './../../domain/helpers/date-time.helper';
import { formatPurchaseDate } from 'src/app/domain/helpers/date-time.helper';
import { GameLibraryService } from './../../domain/service/game-library.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {  ActivatedRoute } from '@angular/router';
import { first } from 'rxjs/operators';
import { Location } from '@angular/common';


@Component({
  selector: 'app-add-update-game',
  templateUrl: './add-update-game.page.html',
  styleUrls: ['./add-update-game.page.scss'],
})
export class AddUpdateGamePage implements OnInit {
  gameForm: FormGroup;
  gameId = '';
  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private gameService: GameLibraryService,
    private location: Location
  ) {}

  ngOnInit() {
    this.gameId = this.activatedRoute.snapshot.queryParamMap.get('gameId');
    if (this.gameId) {
      this.gameService
        .getGameById(this.gameId)
        .pipe(first())
        .subscribe((game: Game) => {
          this.createFormGroup(game);
        });
    } else {
      this.createFormGroup(undefined);
    }
  }

  private createFormGroup(game: Game) {
    this.gameForm = this.formBuilder.group({
      name: [game?.name || '', { validators: [Validators.required] }],
      producer: [
        game?.producer || '',
        {
          validators: [Validators.required],
        },
      ],
      purchaseDate: [
        game ? formatPurchaseDateForm(game.purchaseDate) : '',
        {
          validators: [
            Validators.required,
            Validators.pattern(
              /^(3[01]|[12][0-9]|0[1-9]|[1-9])\/(1[0-2]|0[1-9]|[1-9])\/[0-9]{4}$/
            ),
          ],
        },
      ],
      lastTimePlayed: [
        game ? formatLastTimePlayedForm(game.lastTimePlayed) : '',
        {
          validators: [
            Validators.required,
            Validators.pattern(
              /^(3[01]|[12][0-9]|0[1-9]|[1-9])\/(1[0-2]|0[1-9]|[1-9])\/[0-9]{4} [0-9][0-9]:([0]?[0-5][0-9]|[0-9])$/
            ),
          ],
        },
      ],
      price: [
        game?.price?.priceValue || '',
        {
          validators: [
            Validators.required,
            Validators.pattern(/^(-?)(0|([1-9][0-9]*))(\.[0-9]+)?$/),
          ],
        },
      ],
      currency: [
        Object.keys(Currency)[Object.values(Currency).indexOf(game?.price?.currency)] || '',
        {
          validators: [
            Validators.required,
            Validators.pattern(/^EUR|DOL|RON$/),
          ],
        },
      ],
      platform: [
        game?.platform || '',
        {
          validators: [Validators.required, Validators.pattern(/^PC|PS|XBOX$/)],
        },
      ],
      format: [
        game?.formatOfGame || '',
        {
          validators: [
            Validators.required,
            Validators.pattern(/^DISK|DIGITAL$/),
          ],
        },
      ],
      picture: [
        game?.picture || '',
        {
          validators: [Validators.required],
        },
      ],
    });
  }

  navigateBack(): void {
    this.location.back();
  }

  public getErrorMessage(fieldName: string): string {
    const field = this.gameForm.get(fieldName);
    console.log(field);
    if (field === null) {
      return '';
    }
    if (field.hasError('required')) {
      return 'This field is required';
    }
    if (field.hasError('pattern')) {
      return 'The input does not respect the required format';
    }
  }
  replaceIfExists(): void {
    if (this.gameId !== null) {
      this.gameService.updateGame(this.convertFormToGame(this.gameForm.value));
      this.navigateBack();
      return;
    }
    this.gameService.addGame(this.convertFormToGame(this.gameForm.value));
    this.navigateBack();
  }

  private convertFormToGame(formValue): Game {
    return {
      id: this.gameId || null,
      name: formValue.name,
      producer: formValue.producer,
      purchaseDate: parsePurchaseDateString(formValue.purchaseDate),
      lastTimePlayed: parseLastTimePlayedString(formValue.lastTimePlayed),
      price: {
        priceValue: +formValue.price,
        currency: Currency[formValue.currency],
      },
      platform: Platform[formValue.platform],
      formatOfGame: Format[formValue.format],
      picture: formValue.picture,
    } as Game;
  }
}
