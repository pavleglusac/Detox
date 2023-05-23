import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DiagnoseService {

  constructor(private http: HttpClient) { }

  startDiagnosis = (
    userEmail: string,  
    successCb: (value: any) => void,
    errorCb: (error: any) => void) => {
      this.http
      .post(`api/diagnosis/start?userEmail=${userEmail}`,{})
      .subscribe({
        next(value: any) {
          successCb(value);
        },
        error(err: any) {
          errorCb(err.error);
        },
      });
    }

  setSymptoms = (
    id: number,
    symptom: string,
    successCb: (value: any) => void,
    errorCb: (error: any) => void) => {
      this.http
      .patch(`api/diagnosis/set-symptoms?diagnosisId=${id}&symptomsType=${symptom}`,{})
      .subscribe({
        next(value: any) {
          successCb(value);
        },
        error(err: any) {
          errorCb(err);
        },
      });
    }

    addControlledSubstancesSymptom = (
      id: number,
      data: any,
      successCb: (value: any) => void,
      errorCb: (error: any) => void) => {
        this.http
        .patch(`api/controlled-substances/add?diagnosisId=${id}`,data)
        .subscribe({
          next(value: any) {
            successCb(value);
          },
          error(err: any) {
            errorCb(err);
          },
        });
      }

    addIndustySymptom = (
      id: number,
      data: any,
      successCb: (value: any) => void,
      errorCb: (error: any) => void) => {
        this.http
        .patch(`api/industry/add?diagnosisId=${id}`,data)
        .subscribe({
          next(value: any) {
            successCb(value);
          },
          error(err: any) {
            errorCb(err);
          },
        });
      }

      startGasChromatography = (
        api: string,
        successCb: (value: any) => void,
        errorCb: (error: any) => void) => {
          this.http
          .get(`api/${api}/run-gas-chromatography`,)
          .subscribe({
            next(value: any) {
              successCb(value);
            },
            error(err: any) {
              errorCb(err.error);
            },
          });
        }

        startSpectrophotometry = (
          successCb: (value: any) => void,
          errorCb: (error: any) => void) => {
            this.http
            .get(`api/industry/run-spectophotometry`,)
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
