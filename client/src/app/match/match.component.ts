import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getPromise as getFingerPrint, x64hash128 } from 'fingerprintjs2';
import { Match } from '../objects/match';
import { MatchService } from '../services/match.service';
import { OutputMessage } from '../objects/output-message';
import { InputMessage } from '../objects/input-message';
import { MessageStatus } from '../objects/message-status';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-match',
  templateUrl: './match.component.html',
  styleUrls: ['./match.component.scss']
})
export class MatchComponent implements OnInit {
  private stompClient: any;
  match: Match = new Match();
  playerId: string;

  constructor(private router: Router,
              private matchService: MatchService,
              private snackbar: MatSnackBar) { }

  async ngOnInit() {
    this.playerId = localStorage.getItem('playerId');
    this.match.id = localStorage.getItem('matchId');
    const result = await this.matchService.getMatchById(this.match.id);
    this.match = JSON.parse(result) as Match;
    console.log(this.match);
    this.stompClient = this.matchService.getStompClient();
    this.stompClient.connect({}, (frame) => {
      this.listenJoin();
      this.listenPlay();
    });
  }

  listenJoin() {
    this.stompClient.subscribe('/match/join', (socketResult: any) => {
      const outputMessage = JSON.parse(socketResult.body) as OutputMessage;
      if (outputMessage.type === MessageStatus.ERROR) {
        this.snackbar.open('Error while player join the match!', 'Close', {
          duration: 3000
        });
      } else {
        this.match = outputMessage.match;
      }
    });
  }

  listenPlay() {
    this.stompClient.subscribe('/match/play', (socketResult: any) => {
      const outputMessage = JSON.parse(socketResult.body) as OutputMessage;
      if (outputMessage.type === MessageStatus.ERROR) {
        this.snackbar.open('Error with the play!', 'Close', {
          duration: 3000
        });
      } else {
        this.match = outputMessage.match;
        if (outputMessage.type === MessageStatus.FINISHED) {
          if (outputMessage.match.tie) {
            this.snackbar.open('No winners, it was a tie!', 'Close', {
              duration: 10000
            });
          } else {
            this.snackbar.open(outputMessage.match.winner + ' is the winner!', 'Close', {
              duration: 10000
            });
          }
        } else if (outputMessage.type === MessageStatus.PLAYING) {
          this.snackbar.open(outputMessage.match.playerTurn.name + ' turn!', 'Close', {
            duration: 3000
          });
        }
      }
    });
  }

  leave(): void {
    this.router.navigate(['menu']);
  }

  async play(playPosition: number): Promise<void> {
    const message = new InputMessage();
    message.type = MessageStatus.PLAY;
    message.playerId = localStorage.getItem('playerId');
    message.matchId = localStorage.getItem('matchId');
    message.fingerprint = await this.getWorkstationFingerprint();
    message.playPosition = playPosition;

    this.matchService.sendPlayMessage(this.stompClient, message);
  }

  async getWorkstationFingerprint() {
    const components = await getFingerPrint();
    const values = components.map(component => component.value);
    return x64hash128(values.join(''), 31);
  }
}
