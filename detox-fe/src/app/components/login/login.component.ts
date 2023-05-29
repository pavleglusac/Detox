import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/free-solid-svg-icons';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons/faChevronLeft';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/services/auth.service';
import { UserStateService } from 'src/app/services/store/user-state.service';
import { tokenName } from 'src/app/shared/constants';

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

  constructor(private authenticationService: AuthService, private userStateService: UserStateService, private toastr: ToastrService, private router: Router) {
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
      (value) => {
        this.loadUser(value.accessToken);
      },
      (error: any) => {
        this.toastr.error(error.message, 'Neuspešno prijavljivanje');
        this.errorMessage = 'Neispravni kredencijali.';
      });
    }
    
    loadUser = (token: string) => {
    localStorage.setItem(tokenName, token);
    this.authenticationService.getUser(
      (user: User) => {      
          this.userStateService.setUserState(user);
          this.toastr.success('Uspešna prijava');
          this.router.navigate(['/']); 
      },
      (err: any) => this.toastr.error(err.message)
    ); 

  };

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }
}
