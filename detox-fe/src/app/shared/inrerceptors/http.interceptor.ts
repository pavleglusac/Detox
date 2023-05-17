import {
    HttpEvent,
    HttpHandler,
    HttpHeaders,
    HttpInterceptor,
    HttpRequest,
  } from '@angular/common/http';
  import { Injectable } from '@angular/core';
  import { Observable } from 'rxjs';
  import { baseUrl, tokenName } from '../constants';


  @Injectable()
  export class AuthInterceptor implements HttpInterceptor {
    intercept(
      request: HttpRequest<any>,
      next: HttpHandler
    ): Observable<HttpEvent<any>> {
      request = request.clone({ url: `${baseUrl}/${request.url}`, withCredentials: true });
      const token = sessionStorage.getItem(tokenName);
      if (token) {
        const authReq = request.clone({
          headers: new HttpHeaders({
            Authorization: `Bearer ${token}`,
          }),
        });
        return next.handle(authReq);
      }
      return next.handle(request);
    }
  }
  