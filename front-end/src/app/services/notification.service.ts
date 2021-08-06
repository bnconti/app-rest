import {Injectable} from '@angular/core';
import {MatSnackBar, MatSnackBarConfig} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(public snackBar: MatSnackBar) {
  }

  config: MatSnackBarConfig = {
    duration: 5000,
    horizontalPosition: 'center',
    verticalPosition: 'bottom'
  }

  success(msg: string) {
    this.config['panelClass'] = ['success-notification']
    this.snackBar.open(msg, 'Good!', this.config)
  }

  error(msg: string) {
    this.config['panelClass'] = ['error-notification']
    this.snackBar.open(msg, 'Oh...', this.config)
  }
}
