import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list-item',
  templateUrl: './list-item.component.html',
  styleUrls: ['./list-item.component.scss']
})
export class ListItemComponent implements OnInit {
  @Input() firstPlayerName: string;
  @Input() secondPlayerName: string | undefined;
  @Input() status: string;

  constructor(private router: Router) { }

  ngOnInit() {
  }

  joinGame() {
    this.router.navigate(['match']);
  }

}
