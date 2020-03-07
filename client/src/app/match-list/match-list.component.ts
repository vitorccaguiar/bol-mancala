import { Component, OnInit } from '@angular/core';
import { Match } from '../objects/match';
import { MenuService } from '../services/menu.service';

@Component({
  selector: 'app-match-list',
  templateUrl: './match-list.component.html',
  styleUrls: ['./match-list.component.scss']
})
export class MatchListComponent implements OnInit {
  matches: Match[];

  constructor(private menuService: MenuService) { }

  async ngOnInit() {
    this.refresh();
  }

  async refresh() {
    const returnedMatches = await this.menuService.getAllMatches();
    this.matches = JSON.parse(returnedMatches) as Match[];
  }

}
