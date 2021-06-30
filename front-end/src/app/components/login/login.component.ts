import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '@app/services/authentication.service';
import { faEnvelope } from '@fortawesome/free-solid-svg-icons';
import { faLock } from '@fortawesome/free-solid-svg-icons';
import { first } from 'rxjs/operators';

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
  error = false;

  errorMsj = '';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
    // Redirigir a Home si ya está logueado
    /*
    if (this.authenticationService.currentUser) {
      this.router.navigate(['/']);
    }
    */

    this.loginForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    if (localStorage.getItem("JWT")) {
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
      .pipe(first())
      .subscribe({
        next: () => {
          // const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
          this.router.navigate(['/home']);
        },
        error: () => {
          this.error = true;
          this.errorMsj = "Invalid username or password.";
          this.loading = false;
        }
      })
  }

}
