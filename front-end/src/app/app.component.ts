import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  title = 'front-end';
  // TODO: loggedIn es un stub, implementar
  loggedIn: boolean = false;
}
