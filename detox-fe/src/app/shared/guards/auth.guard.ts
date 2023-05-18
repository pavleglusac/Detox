import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { tokenName } from '../constants';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {

  constructor(private router: Router) { }

  canActivate(): boolean {
    let token: string | null = window.localStorage.getItem(tokenName);
    if (!token) {
      this.router.navigate(['/login']);
      return false;
    }
    
    return true;
  }
}
