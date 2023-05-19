import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  login = (
    email: string, 
    password: string, 
    successCb: (value: any) => void,
    errorCb: (error: any) => void) => {
      this.http
      .post('api/auth/login', { email, password})
      .subscribe({
        next(value: any) {
          successCb(value);
        },
        error(err: any) {
          errorCb(err.error);
        },
      });
    }

  getUser = (successCb: (user: User) => void, errorCb: (error: any) => void) => {
    this.http.get('api/auth/my-profile')
    .subscribe({
      next(value: any) {
        successCb(value);
      },
      error(err) {
        errorCb(err.error);
      },
    })
  }

  logout = (successCb: () => void) => {
    this.http
    .post('api/auth/logout', {}, { responseType: 'text' })
    .subscribe({
      next(value: any) {
        successCb();
      },
      error(err) {
        console.log(err)
      },
    })
  }
}
