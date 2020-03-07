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
    this.connect();
  }

  connect() {
    const socket = new SockJS(environment.websocketUrl);
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        this.stompClient.subscribe('/match/join', (outputMessage: OutputMessage) => {
          console.log('Returned message');
          console.log(outputMessage);
        });
    });
  }

  disconnect() {
      if (this.stompClient !== null) {
          this.stompClient.disconnect();
      }
      console.log('Disconnected');
  }

  sendJoinMessage(message: InputMessage) {
      this.stompClient.send('/app/join', {}, JSON.stringify(message));
  }
}
