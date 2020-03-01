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
  match: Match;

  constructor(private router: Router,
              private matchService: MatchService) { }

  async ngOnInit() {
    const matchId = localStorage.getItem('matchId');
    let returnedMatch = await this.matchService.getMatchById(matchId);
    returnedMatch = JSON.parse(returnedMatch) as Match;
    this.match = returnedMatch;
    console.log(this.match);
  }

  leave(): void {
    localStorage.removeItem('matchId');
    this.router.navigate(['menu']);
  }

  async play(player: number, position: number): Promise<void> {

  }
}
