import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Play } from '../objects/play';
import { GameService } from '../services/game.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {
  firstPlayerName = 'Vitor';
  secondPlayerName = 'Loraine';
  firstPlayerPits = [];
  secondPlayerPits = [];

  constructor(private router: Router, private gameService: GameService) { }

  ngOnInit() {
  }

  backToMenu(): void {
    this.router.navigate(['menu']);
  }

  play(player: number, position: number): void {
    const play = new Play();
    play.player = player;
    play.position = position;
    this.gameService.play(play);
  }
}
