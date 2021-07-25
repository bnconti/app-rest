import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";

import { faEnvelope } from '@fortawesome/free-solid-svg-icons';
import { faLock } from '@fortawesome/free-solid-svg-icons';
import { Router } from "@angular/router";
import { SignupService } from "@services/signup.service";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.sass']
})
export class SignupComponent implements OnInit {

  emailRegExp = new RegExp('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$');

  signupForm: FormGroup;

  faEnvelope = faEnvelope;
  faLock = faLock;

  loading = false;
  submitted = false;
  signupError = false;
  signupErrorMsg = '';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private signupService: SignupService
  ) {
    this.signupForm = this.formBuilder.group({
      email: [null, [Validators.required, Validators.pattern(this.emailRegExp)]],
      passwords: this.formBuilder.group({
        password: [null, [Validators.required, Validators.minLength(6)]],
        passwordConfirmation: [null, [Validators.required]]
      }, {
        validators: this.checkPasswords('password', 'passwordConfirmation')
      })
    }, {});
  }

  get f() { return this.signupForm.controls; }

  get pass() {
    // TODO: ver de acomodar para no tener que usar el ! en el HTML
    return this.f.passwords.get('password');
    // return (this.signupForm.get('passwords') as FormArray).controls;
    // @ts-ignore
    // return this.signupForm.get('passwords')['controls'];
  }

  get confPass() {
    return this.f.passwords.get('passwordConfirmation');
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.signupForm.invalid) {
      return;
    }

    this.loading = true;

    const email = this.f.email.value;
    const password = this.pass!.value;

    this.signupService.signup(email, password)
      .subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: () => {
          this.showError("There was a problem while creating your account.");
        }
      })
  }

  checkPasswords(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      // Si ya tiene otro error, muestro sólo ese
      if (matchingControl.errors && !matchingControl.errors.confirmedValidator) {
        return;
      }

      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ invalidConfirmation: true });
      } else {
        matchingControl.setErrors(null);
      }
    }
  }

  showError(msg: string): void {
    this.signupError = true;
    this.signupErrorMsg = msg;
    this.loading = false;
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

}
