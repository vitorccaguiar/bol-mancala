import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Play } from '../objects/play';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(private httpClient: HttpClient) { }

  getHeaders(): any {
    return {
      responseType: 'application/json',
    };
  }

  play(play: Play): Promise<any> {
    return this.httpClient.post<any>(
      `${environment.apiUrl}/`,
      play,
      this.getHeaders(),
    ).toPromise();
  }

}
