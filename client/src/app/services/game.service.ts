import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Play } from '../objects/play';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(private httpClient: HttpClient) { }

  play(play: Play): Promise<any> {
    return this.httpClient.post<any>(
      `${environment.apiUrl}/`,
      play,
    ).toPromise();

  }
}
