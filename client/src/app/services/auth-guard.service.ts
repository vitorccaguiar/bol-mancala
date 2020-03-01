import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(private router: Router) { }

  canActivate(): boolean {
    if (!this.hasNameRegistered()) {
      this.router.navigate(['register']);
      return false;
    }
    return true;
  }

  hasNameRegistered(): boolean {
    if (localStorage.getItem('playerName')) {
      return true;
    } else {
      return false;
    }
  }

}