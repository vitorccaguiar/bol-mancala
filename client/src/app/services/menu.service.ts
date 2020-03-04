import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BaseService } from './base.service';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MenuService extends BaseService {

  constructor(private httpClient: HttpClient) {
    super();
  }

  createMatch(playerId: string, fingerprint: string): Promise<any> {
    return this.httpClient.post<any>(
      `${environment.apiUrl}/menu/new`,
      {playerId, fingerprint},
      this.getHeaders(),
    ).toPromise();
  }

  getAllMatches(): Promise<any> {
    return this.httpClient.get<any>(
      `${environment.apiUrl}/menu/match`,
      this.getHeaders(),
    ).toPromise();
  }
}
