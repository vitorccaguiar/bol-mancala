import { Injectable, ɵConsole } from '@angular/core';
import { BaseService } from './base.service';
import { HttpClient, HttpEvent } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Match } from '../objects/match';

@Injectable({
  providedIn: 'root'
})
export class MatchService extends BaseService {

  constructor(private httpClient: HttpClient) {
    super();
  }

  saveMatch(match: Match): Promise<any> {
    return this.httpClient.post<any>(
      `${environment.apiUrl}/match`,
      match,
      this.getHeaders(),
    ).toPromise();
  }

  getAllMatches(): Promise<any> {
    return this.httpClient.get<Match[]>(
      `${environment.apiUrl}/match`,
      this.getHeaders(),
    ).toPromise();
  }

  getMatchById(id: string): Promise<any> {
    return this.httpClient.get<Match>(
      `${environment.apiUrl}/match/${id}`,
      this.getHeaders(),
    ).toPromise();
  }
}
