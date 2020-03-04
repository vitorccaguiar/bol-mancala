import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getPromise as getFingerPrint, x64hash128 } from 'fingerprintjs2';
import { UserService } from '../services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatchService } from '../services/match.service';
import { Match } from '../objects/match';
import { Status } from '../objects/status';
import { MenuService } from '../services/menu.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {
  playerName: string;

  constructor(private router: Router,
              private userService: UserService,
              private menuService: MenuService,
              private snackbar: MatSnackBar) { }

  ngOnInit() {
    this.playerName = localStorage.getItem('playerName');
  }

  async newGame() {
    try {
      const returnedMatch = await this.menuService.createMatch(
        localStorage.getItem('playerId'), await this.getWorkstationFingerprint());
      localStorage.setItem('matchId', (JSON.parse(returnedMatch) as Match).id);
      this.router.navigate(['match']);
    } catch (e) {
      this.snackbar.open('Error while creating the match!', 'Close', {
        duration: 3000
      });
    }
  }

  changeName() {
    try {
      const playerId = localStorage.getItem('playerId');
      if (playerId) {
        this.userService.deleteUser(playerId);
        localStorage.removeItem('playerName');
        localStorage.removeItem('playerId');
        this.router.navigate(['register']);
      } else {
        this.snackbar.open('Error while removing current name!', 'Close', {
          duration: 3000
        });
      }
    } catch (e) {
      this.snackbar.open('Error while removing current name!', 'Close', {
        duration: 3000
      });
    }
  }

  async getWorkstationFingerprint() {
    const components = await getFingerPrint();
    const values = components.map(component => component.value);
    return x64hash128(values.join(''), 31);
  }

}
