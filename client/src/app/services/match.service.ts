import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { environment } from '../../environments/environment';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { OutputMessage } from '../objects/output-message';
import { InputMessage } from '../objects/input-message';

@Injectable({
  providedIn: 'root'
})
export class MatchService extends BaseService {
  private stompClient: any;

  constructor() {
    super();
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection() {
    const websocket = new SockJS(environment.socketUrl);
    this.stompClient = Stomp.over(websocket);
    this.stompClient.connect({}, function() {
      this.stompClient.subscribe('/messages', (message: OutputMessage) => {

      });
    });
  }

  sendMessage(message: InputMessage) {
    this.stompClient.send('/game/match', {}, message);
  }

}
