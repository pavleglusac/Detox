import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Diagnosis } from 'src/app/model/diagnose';


@Injectable({
  providedIn: 'root'
})
export class DiagnosisStateService {
  private diagnosisState$ = new BehaviorSubject<Diagnosis | null>(null);

  constructor() {
    const savedState = sessionStorage.getItem('diagnosisState');

    if (savedState) {
      const parsedState = JSON.parse(savedState);
      this.diagnosisState$.next(parsedState);
    }
  }

  getDiagnosisState(): Observable<Diagnosis | null> {
    return this.diagnosisState$.asObservable();
  }

  setDiagnosisState(diagnosis: Diagnosis): void {
    this.diagnosisState$.next(diagnosis);
    sessionStorage.setItem('diagnosisState', JSON.stringify(diagnosis));
  }

  clearDiagnosisState(): void {
    this.diagnosisState$.next(null);
    sessionStorage.removeItem('diagnosisState');
  }
}
