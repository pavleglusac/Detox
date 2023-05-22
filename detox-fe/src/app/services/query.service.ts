import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class QueryService {

  constructor(private http: HttpClient) { }

  search = (
      api: string,
      input: string,  
      successCb: (value: any) => void,
      errorCb: (error: any) => void,
      ) => {
      this.http
      .get(`api/queries/${api}${input}`)
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
