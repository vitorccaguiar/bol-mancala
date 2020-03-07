import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Match } from '../objects/match';
import { MatchService } from '../services/match.service';

@Component({
  selector: 'app-match',
  templateUrl: './match.component.html',
  styleUrls: ['./match.component.scss']
})
export class MatchComponent implements OnInit {
  match: Match = new Match();

  constructor(private router: Router,
              private matchService: MatchService) { }

  async ngOnInit() {
    this.match.id = localStorage.getItem('matchId');
  }

  leave(): void {
    this.router.navigate(['menu']);
  }

  async play(player: number, position: number): Promise<void> {

  }
}
