import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { User } from '../objects/user';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  playerName: string;

  constructor(private router: Router,
              private userService: UserService,
              private snackbar: MatSnackBar
    ) { }

  ngOnInit() {
  }

  async saveName() {
    const user = new User();
    user.name = this.playerName;
    try {
      let createdUser = await this.userService.createUser(user);
      createdUser = JSON.parse(createdUser) as User;
      localStorage.setItem('playerId', createdUser.id);
      localStorage.setItem('playerName', createdUser.name);
      this.snackbar.open('Successfuly created your name!', 'Close', {
        duration: 3000
      });
      this.router.navigate(['menu']);
    } catch (error) {
      this.snackbar.open('Error while creating your name!', 'Close', {
        duration: 3000
      });
    }
  }
}
