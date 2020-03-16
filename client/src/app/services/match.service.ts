import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { environment } from '../../environments/environment';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { InputMessage } from '../objects/input-message';
import { Match } from '../objects/match';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MatchService extends BaseService {

  constructor(private httpClient: HttpClient) {
    super();
  }

  getStompClient(): any {
    const socket = new SockJS(environment.websocketUrl);
    return Stomp.over(socket);
  }

  sendJoinMessage(stompClient: any, message: InputMessage): void {
    stompClient.send('/app/join', {}, JSON.stringify(message));
  }

  sendPlayMessage(stompClient: any, message: InputMessage): void {
    stompClient.send('/app/play', {}, JSON.stringify(message));
  }

  sendLeaveMessage(stompClient: any, message: InputMessage): void {
    stompClient.send('/app/leave', {}, JSON.stringify(message));
  }

  getMatchById(matchId: string): Promise<any> {
    return this.httpClient.get<any>(
      `${environment.apiUrl}/match/?matchId=${matchId}`,
      this.getHeaders(),
    ).toPromise();
  }
}
