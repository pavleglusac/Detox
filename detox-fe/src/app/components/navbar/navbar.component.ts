import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

interface MenuOption {
  title: string;
  link: string;
}
const doctorMenu: MenuOption[] = [
  { title: 'Diagnose', link: '' },
  { title: 'Queries', link: 'query' },
  { title: 'Settings', link: 'settings' },
  { title: 'Profile', link: 'profile' },

]

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule]
})
export class NavbarComponent {

  route: string | null = null;

  constructor(private router: Router) { 
    this.route = this.router.url.split('/')[1]
  }

  navigate = (route: string) => {
    this.router.navigate([route]);
    this.route = route;
  }

  get options(): MenuOption[] {
    return doctorMenu;
  }
}
