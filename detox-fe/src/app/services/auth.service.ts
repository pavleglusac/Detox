import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  login = (
    email: string, 
    password: string, 
    successCb: () => void,
    errorCb: (error: any) => void) => {
      this.http
      .post('api/auth/login', { email, password})
      .subscribe({
        next(value: any) {
          successCb();
        },
        error(err: any) {
          errorCb(err.error);
        },
      });
    }
}
