import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { User } from '../objects/user';
import { BaseService } from './base.service';

@Injectable({
  providedIn: 'root'
})
export class UserService extends BaseService {

  constructor(private httpClient: HttpClient) {
    super();
   }

  createUser(user: User): Promise<any> {
    return this.httpClient.post<any>(
      `${environment.apiUrl}/user`,
      user,
      this.getHeaders(),
    ).toPromise();
  }

  deleteUser(playerId: string): Promise<any> {
    return this.httpClient.delete<any>(
      `${environment.apiUrl}/user/${playerId}`,
      this.getHeaders(),
    ).toPromise();
  }
}
