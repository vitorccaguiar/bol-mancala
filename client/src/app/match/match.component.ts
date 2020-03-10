import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getPromise as getFingerPrint, x64hash128 } from 'fingerprintjs2';
import { Match } from '../objects/match';
import { MatchService } from '../services/match.service';
import { OutputMessage } from '../objects/output-message';
import { InputMessage } from '../objects/input-message';
import { MessageStatus } from '../objects/message-status';

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
    this.stompClient = this.matchService.getStompClient();
    this.stompClient.connect({}, (frame) => {
      this.listenJoin();
      this.listenPlay();
    });
  }

  listenJoin() {
    this.stompClient.subscribe('/match/join', (outputMessage: OutputMessage) => {
      this.match = outputMessage.match;
    });
  }

  listenPlay() {
    this.stompClient.subscribe('/match/play', (outputMessage: OutputMessage) => {
      this.match = outputMessage.match;
    });
  }

  leave(): void {
    this.router.navigate(['menu']);
  }

  async play(position: number): Promise<void> {
    const message = new InputMessage();
    message.type = MessageStatus.PLAY;
    message.playerId = localStorage.getItem('playerId');
    message.fingerprint = await this.getWorkstationFingerprint();

    this.matchService.sendPlayMessage(this.stompClient, message);
  }

  async getWorkstationFingerprint() {
    const components = await getFingerPrint();
    const values = components.map(component => component.value);
    return x64hash128(values.join(''), 31);
  }
}
