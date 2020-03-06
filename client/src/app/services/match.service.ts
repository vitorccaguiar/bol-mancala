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
    const socket = new SockJS('http://localhost:8080/mancala-websocket');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        this.stompClient.subscribe('/match/greetings', (greeting) => {
          console.log('Subscribed');
        });
    });
  }

  disconnect() {
      if (this.stompClient !== null) {
          this.stompClient.disconnect();
      }
      console.log('Disconnected');
  }

  sendName() {
      this.stompClient.send('/app/hello', {}, 'Message');
  }
}
