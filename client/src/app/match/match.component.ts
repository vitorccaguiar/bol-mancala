import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Match } from '../objects/match';
import { MatchService } from '../services/match.service';
import { OutputMessage } from '../objects/output-message';

@Component({
  selector: 'app-match',
  templateUrl: './match.component.html',
  styleUrls: ['./match.component.scss']
})
export class MatchComponent implements OnInit {
  private stompClient: any;
  match: Match = new Match();

  constructor(private router: Router,
              private matchService: MatchService) { }

  async ngOnInit() {
    this.match.id = localStorage.getItem('matchId');
    const result = await this.matchService.getMatchById(this.match.id);
    this.match = JSON.parse(result) as Match;
    console.log(this.match.playerTurn.name);
    this.stompClient = this.matchService.getStompClient();
    this.stompClient.connect({}, (frame) => {
      this.listenJoin();
    });
  }

  listenJoin() {
    this.stompClient.subscribe('/match/join', (outputMessage: OutputMessage) => {
      this.match = outputMessage.match;
    });
  }

  leave(): void {
    this.router.navigate(['menu']);
  }

  async play(player: number, position: number): Promise<void> {

  }
}
