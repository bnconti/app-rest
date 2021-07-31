import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '@app/services/authentication.service';
import { faEnvelope, faLock } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {
  // El formulario en el que el usuario ingresó sus credenciales
  loginForm: FormGroup;

  // Íconos que utiliza el componente
  faEnvelope = faEnvelope;
  faLock = faLock;

  // Variables para modificar dinámicamente la página según lo que se está haciendo
  // Por ej. deshabilitar el botón de login si ya se está procesando una solicitud.
  loading = false;
  submitted = false;
  loginError = false;
  loginErrorMsg = '';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
    this.loginForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    if (localStorage.getItem("currentUser")) {
      // Si ya está logueado lo mando para Home
      this.router.navigate(['/home']);
    }
  }

  // Atajo para acceder a los elementos del formulario
  get f() { return this.loginForm.controls; }

  onSubmit(): void {

    this.submitted = true;

    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;

    const email = this.loginForm.controls['email'].value;
    const password = this.loginForm.controls['password'].value;

    this.authenticationService.login(email, password)
      .subscribe({
        next: () => {
          this.router.navigate(['/home']);
        },
        error: () => {
          this.loginError = true;
          this.loginErrorMsg = "Invalid username or password, please try again.";
          this.loading = false;
        }
      })
  }

  goToSignup() {
    this.router.navigate(['/signup']);
  }

}
