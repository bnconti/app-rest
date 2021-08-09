import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass']
})
export class HomeComponent {

  constructor() { }

  getEmail(): string {
    return JSON.parse(sessionStorage.getItem('currentUser')!).email
  }

}
