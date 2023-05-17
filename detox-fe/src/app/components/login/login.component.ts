import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/free-solid-svg-icons';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons/faChevronLeft';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, ReactiveFormsModule]
})
export class LoginComponent {
  faChevronLeft: IconDefinition = faChevronLeft;
  errorMessage: string = '';

  showPasswordChangeResponseModal: boolean = false;
  passwordChangeResponseModalTitle: string = '';
  passwordChangeResponseModalMessage: string = '';

  loginForm = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.pattern(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/)
    ]),
    password: new FormControl('', [
      Validators.required
    ]),
  });

  constructor(private authenticationService: AuthService, private toastr: ToastrService) { 
    document.getElementById('login-email')?.focus();
  }

  async onSubmit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    };
    await this.authenticationService.login(
      this.email?.value!,
      this.password?.value!,
      () => {
        this.toastr.success('Login successful');
        window.location.href = '/home';
      },
      (error: any) => {
        this.toastr.error(error.message, 'Login failed');
        this.errorMessage = 'Incorrect credentials.';
      });
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }
}
