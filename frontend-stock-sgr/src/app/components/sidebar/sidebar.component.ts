import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  activeLink:string="acceuil"
  constructor(
    private router:Router
  ) { }

  ngOnInit(): void {
    console.log(this.router.url)
  }

  onActiveLink(text:string){this.activeLink = text}

}
