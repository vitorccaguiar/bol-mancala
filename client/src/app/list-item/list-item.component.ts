import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { getPromise as getFingerPrint, x64hash128 } from 'fingerprintjs2';
import { Match } from '../objects/match';
import { MatchService } from '../services/match.service';
import { InputMessage } from '../objects/input-message';
import { MessageStatus } from '../objects/message-status';

@Component({
  selector: 'app-list-item',
  templateUrl: './list-item.component.html',
  styleUrls: ['./list-item.component.scss']
})
export class ListItemComponent implements OnInit {
  @Input() match: Match;

  constructor(private router: Router,
              private matchService: MatchService) { }

  ngOnInit() {
  }

  async joinGame() {
    const message = new InputMessage();
    message.type = MessageStatus.JOIN;
    message.playerId = localStorage.getItem('playerId');
    message.fingerprint = await this.getWorkstationFingerprint();
    message.match = this.match;

    this.matchService.sendJoinMessage(message);
    localStorage.setItem('matchId', this.match.id);
    this.router.navigate(['match']);
  }

  async getWorkstationFingerprint() {
    const components = await getFingerPrint();
    const values = components.map(component => component.value);
    return x64hash128(values.join(''), 31);
  }

}
