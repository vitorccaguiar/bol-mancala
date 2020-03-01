import { Component, OnInit } from '@angular/core';
import { Match } from '../objects/match';
import { MatchService } from '../services/match.service';

@Component({
  selector: 'app-match-list',
  templateUrl: './match-list.component.html',
  styleUrls: ['./match-list.component.scss']
})
export class MatchListComponent implements OnInit {
  matches: Match[];

  constructor(private matchService: MatchService) { }

  async ngOnInit() {
    const returnedMatches = await this.matchService.getAllMatches();
    this.matches = JSON.parse(returnedMatches) as Match[];
  }

}
