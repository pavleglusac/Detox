import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Diagnosis } from 'src/app/model/diagnose';
import { DiagnoseService } from 'src/app/services/diagnose.service';
import { DiagnosisStateService } from 'src/app/services/store/diagnosis-state.service';

@Component({
  selector: 'app-diagnose',
  templateUrl: './diagnose.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class DiagnoseComponent {
  diagnosis: Diagnosis | null = null;
  loginForm = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.pattern(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/)
    ])})

  constructor(private diagnoseService: DiagnoseService, private toastr: ToastrService, private diagnosisStateService: DiagnosisStateService) { 
    diagnosisStateService.getDiagnosisState().subscribe((diagnosis: Diagnosis | null) => {
      this.diagnosis = diagnosis;
    });
  }

  onSubmit = () => {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    };
    this.diagnoseService.startDiagnosis(
      this.email!.value!,
      (value: any) => {
        this.diagnosis = new Diagnosis(value);
      },
      (error: any) => {
        this.diagnosis = null;
        this.toastr.error(error.message);
      }
    );
  }

  get email() {
    return this.loginForm.get('email');
  }

}
