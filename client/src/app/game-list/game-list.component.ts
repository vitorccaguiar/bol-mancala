import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-game-list',
  templateUrl: './game-list.component.html',
  styleUrls: ['./game-list.component.scss']
})
export class GameListComponent implements OnInit {
  games = [
    {
      firstPlayerName: 'Vitor',
      secondPlayerName: 'Loraine',
      status: 'Playing'
    },
    {
      firstPlayerName: 'Vitor',
      secondPlayerName: '<<Empty>>',
      status: 'Waiting player'
    },
    {
      firstPlayerName: 'Vitor',
      secondPlayerName: 'Chalk',
      status: 'Playing'
    },
    {
      firstPlayerName: 'Chalk',
      secondPlayerName: '<<Empty>>',
      status: 'Waiting player'
    },
  ];

  constructor() { }

  ngOnInit() {
  }

}
