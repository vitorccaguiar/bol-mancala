import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Match } from '../objects/match';

@Component({
  selector: 'app-list-item',
  templateUrl: './list-item.component.html',
  styleUrls: ['./list-item.component.scss']
})
export class ListItemComponent implements OnInit {
  @Input() match: Match;

  constructor(private router: Router) { }

  ngOnInit() {
  }

  joinGame() {
    this.router.navigate(['match']);
  }

}
