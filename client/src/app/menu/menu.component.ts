import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatchService } from '../services/match.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {
  playerName: string;

  constructor(private router: Router,
              private userService: UserService,
              private matchService: MatchService,
              private snackbar: MatSnackBar) { }

  ngOnInit() {
    this.playerName = localStorage.getItem('playerName');
  }

  newGame() {

    this.router.navigate(['game']);
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

}
