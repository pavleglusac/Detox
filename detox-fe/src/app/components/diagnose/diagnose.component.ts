import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { DiagnoseService } from 'src/app/services/diagnose.service';

@Component({
  selector: 'app-diagnose',
  templateUrl: './diagnose.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class DiagnoseComponent {
  started: boolean = false;
  loginForm = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.pattern(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/)
    ])})

  constructor(private diagnoseService: DiagnoseService, private toastr: ToastrService) { }

  onSubmit = () => {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    };
    this.diagnoseService.startDiagnosis(
      this.email!.value!,
      (value: any) => {
        this.started = true;
      },
      (error: any) => {
        this.started = false;
        this.toastr.error(error.message);
      }
    );
  }

  get email() {
    return this.loginForm.get('email');
  }

}
