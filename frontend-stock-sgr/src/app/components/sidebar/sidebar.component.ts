import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  activeLink:string="acceuil"
  constructor() { }

  ngOnInit(): void {
  }

  onActiveLink(text:string){this.activeLink = text}

}
