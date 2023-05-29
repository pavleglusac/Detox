import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from 'src/app/model/user';

@Injectable({
  providedIn: 'root'
})
export class UserStateService {
  private userState$ = new BehaviorSubject<User | null>(null);

  constructor() {
    const savedState = localStorage.getItem('userState');

    if (savedState) {
      const parsedState = JSON.parse(savedState);
      this.userState$.next(parsedState);
    }
  }

  getUserState(): Observable<User | null> {
    return this.userState$.asObservable();
  }

  setUserState(user: User): void {
    this.userState$.next(user);
    localStorage.setItem('userState', JSON.stringify(user));
  }

  clearUserState(): void {
    this.userState$.next(null);
    localStorage.removeItem('userState');
  }
}
