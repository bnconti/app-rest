import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators
} from "@angular/forms";

import { faEnvelope, faLock, faUserPlus, faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { Router } from "@angular/router";
import { SignupService } from "@services/signup.service";
import { NotificationService } from "@services/notification.service";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.sass']
})
export class SignupComponent {

  emailRegExp = new RegExp('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$');

  signUpForm: FormGroup;

  faEnvelope = faEnvelope;
  faLock = faLock;
  faUserPlus = faUserPlus;
  faArrowLeft = faArrowLeft;

  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private signupService: SignupService,
    private notificationService: NotificationService
  ) {
    this.signUpForm = this.formBuilder.group({
      email: [null, [Validators.required, Validators.pattern(this.emailRegExp)]],
      passwords: this.formBuilder.group({
        password: [null, [Validators.required, Validators.minLength(6)]],
        passwordConfirmation: [null, [Validators.required]]
      }, {
        validators: this.checkIfPasswordsMatch('password', 'passwordConfirmation'),
      })
    }, {
      validators: this.checkIfEmailIsAvailable('email')
    });
  }

  get f() { return this.signUpForm.controls; }

  get pass() {
    return this.f.passwords.get('password');
  }

  get passConf() {
    return this.f.passwords.get('passwordConfirmation');
  }

  onSubmit(): void {
    this.submitted = true;
    //console.log(this.f.email.hasError('required'));

    if (this.signUpForm.invalid) {
      this.notificationService.error("Please, check the form fields");
      return;
    }

    this.loading = true;
    const email = this.f.email.value;
    const password = this.pass!.value;

    this.signupService.signup(email, password)
      .subscribe({
        next: () => {
          this.loading = false;
          this.notificationService.success("New account created successfully.");
          this.router.navigate(['/login']);
        },
        error: () => {
          this.loading = false;
          this.notificationService.error("Something went wrong while creating your account.\nPerhaps the service is not running?");
        }
      })
  }

  checkIfPasswordsMatch(passControlName: string, passConfControlName: string) {
    return (formGroup: FormGroup) => {
      const passControl = formGroup.controls[passControlName];
      const passConfControl = formGroup.controls[passConfControlName];

      // Si ya tiene otro error, muestro sólo ese
      if (passConfControl.errors && !passConfControl.errors.confirmedValidator) {
        return;
      }

      if (passControl.value !== passConfControl.value) {
        passConfControl.setErrors({ invalidConfirmation: true });
      } else {
        passConfControl.setErrors(null);
      }
    }
  }

  checkIfEmailIsAvailable(emailControlName: string) {
    return (formGroup: FormGroup) => {

    if (formGroup.controls[emailControlName].errors) {
      return;
    }

    const email = formGroup.controls[emailControlName].value;

    this.signupService.emailExists(email)
      .subscribe({
        next: (res) => {
          if (res) {
            this.f.email.setErrors({ unavailable : true})
          } else {
            this.f.email.setErrors(null)
          }
        },
        // Si no funciona esto se muestra un error en onSubmit()
        error: () => {
        }
      })
    }
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

}
