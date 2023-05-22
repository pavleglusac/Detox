import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class QueryService {

  constructor(private http: HttpClient) { }

  search = (
    input: string,  
    successCb: (value: any) => void,
    errorCb: (error: any) => void) => {
      this.http
      .post(`api/diagnosis/start?`,{})
      .subscribe({
        next(value: any) {
          successCb(value);
        },
        error(err: any) {
          errorCb(err.error);
        },
      });
    }
}
