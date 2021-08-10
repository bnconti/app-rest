import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '@app/services/authentication.service';
import { faEnvelope, faLock, faDoorOpen, faUserPlus } from '@fortawesome/free-solid-svg-icons';
import { NotificationService } from "@services/notification.service";

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
  faDoorOpen = faDoorOpen;
  faUserPlus = faUserPlus;

  // Variables para modificar dinámicamente la página según lo que se está haciendo
  // Por ej. deshabilitar el botón de login si ya se está procesando una solicitud.
  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authenticationService: AuthenticationService,
    private notificationService: NotificationService
  ) {
    this.loginForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    if (sessionStorage.getItem("currentUser")) {
      // Si ya está logueado lo mando para Home
      this.router.navigate(['/home']);
    }
  }

  // Atajo para acceder a los elementos del formulario
  get f() { return this.loginForm.controls; }

  onSubmit(): void {
    this.submitted = true;

    if (this.loginForm.invalid) {
      this.notificationService.error("Please, check the form fields.");
      return;
    }

    this.loading = true;

    const email = this.loginForm.controls['email'].value;
    const password = this.loginForm.controls['password'].value;

    this.authenticationService.login(email, password)
      .subscribe(
        next => {
          this.notificationService.success("Logged in successfully!")
          this.router.navigate(['/home']);
        },
        error => {
          // Esto es un parche bastante feo...
          // Esta es la descripción del error que da si no se puede conectar al servicio
          if (error == "Unknown Error") {
            this.notificationService.error("Something went wrong when trying to log you in. Perhaps the service is not running?");
          } else {
            this.notificationService.error("Invalid username or password. Please, try again.");
          }
          this.loading = false;
        }
      )
  }

  goToSignup() {
    this.router.navigate(['/signup']);
  }

}
