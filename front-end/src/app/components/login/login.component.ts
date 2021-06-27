import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { faEnvelope } from '@fortawesome/free-solid-svg-icons';
import { faLock } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {

  // Íconos que utiliza el componente
  faEnvelope = faEnvelope;
  faLock = faLock;

  loginForm = this.formBuilder.group({
    email: '',
    password: '',
  });

  constructor(private formBuilder: FormBuilder) {

  }

  ngOnInit(): void {
  }

  processLoginForm(): void {
    // TODO
    const email = this.loginForm.controls['email'].value;
    const password = this.loginForm.controls['password'].value;
    const creds = `Email: ${email}\nPassword: ${password}`;
    alert(creds);
  }

}
