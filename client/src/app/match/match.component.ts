import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-match',
  templateUrl: './match.component.html',
  styleUrls: ['./match.component.scss']
})
export class MatchComponent implements OnInit {
  firstPlayerName = 'Vitor';
  secondPlayerName = 'Loraine';
  firstPlayerPits = [];
  secondPlayerPits = [];

  constructor(private router: Router) { }

  ngOnInit() {
  }

  backToMenu(): void {
    this.router.navigate(['menu']);
  }

  async play(player: number, position: number): Promise<void> {

  }
}
