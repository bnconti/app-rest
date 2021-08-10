import {MAT_DIALOG_DATA} from '@angular/material/dialog';

import {Component, Inject} from '@angular/core';
import {DialogData} from "@app/interfaces/DialogData";

import {faTimes} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.sass']
})
export class ConfirmationDialogComponent {

  faTimes = faTimes;

  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) {}
}
