import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/services/auth.service';
import { UserStateService } from 'src/app/services/store/user-state.service';
import { tokenName } from 'src/app/shared/constants';

interface MenuOption {
  title: string;
  link: string;
}
const doctorMenu: MenuOption[] = [
  { title: 'Dijagnoza', link: '' },
  { title: '  Upiti  ', link: 'query' },
  { title: 'Podešavanja', link: 'settings' },
  { title: 'Moj profil', link: 'profile' },
]

const patientMenu: MenuOption[] = [
  { title: 'Moj profil', link: 'profile' },
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
  user : User | null = null;

  constructor(private router: Router, private authService: AuthService, private userStateService: UserStateService) { 
    this.route = this.router.url.split('/')[1]
    this.userStateService.getUserState().subscribe((user: User | null) => {
      this.user = user;
    });
    if (this.user?.role === 'ROLE_PATIENT') {
      this.navigate('profile');
    }
  }

  navigate = (route: string) => {
    this.router.navigate([route]);
    this.route = route;
  }

  get options(): MenuOption[] {
    if (this.user?.role === 'ROLE_PATIENT') {
      return patientMenu;
    }
    return doctorMenu;
  }

  logout = (): void => {
    this.authService.logout(()=> {
      localStorage.removeItem(tokenName);
      this.userStateService.clearUserState();
      this.router.navigate(['/login']);
    });
    
  }
}
