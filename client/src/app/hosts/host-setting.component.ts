import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-host-settings',
  templateUrl: './host-setting.component.html',
  styleUrls: ['./host-setting.component.scss'],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatOptionModule,
    MatIcon,
  ]
})
export class HostSettingsComponent {

  addPromptForm = new FormGroup({
    prompt: new FormControl('', Validators.compose([
      Validators.required,
      Validators.minLength(5),
      Validators.maxLength(200)
    ]))
  });

  readonly addPromptValidationMessages = {
    prompt: [
      { type: 'required', message: 'Prompt is required' },
      { type: 'maxlength', message: 'Prompt cannot be more than 200 characters long' }
    ]
  };

  constructor(
    private snackBar: MatSnackBar,
    private router: Router) {
  }

  formControlHasError(controlName: string): boolean {
    return this.addPromptForm.get(controlName).invalid &&
      (this.addPromptForm.get(controlName).dirty || this.addPromptForm.get(controlName).touched);
  }

  getErrorMessage(name: keyof typeof this.addPromptValidationMessages): string {
    for (const { type, message } of this.addPromptValidationMessages[name]) {
      if (this.addPromptForm.get(name).hasError(type)) {
        return message;
      }
    }
    return 'Unknown error';
  }

  /**
   * Handles the submission of the prompt form.
   *
   * This method checks if the form is valid. If valid, it retrieves the prompt value from the form,
   * logs the prompt to the console, displays a snackbar notification indicating the prompt was added,
   * and then resets the form. If the form is not valid, it displays a snackbar notification asking the user
   * to correct the errors in the form.
   */

  submitPrompt() {
    if (this.addPromptForm.valid) {
      const prompt = this.addPromptForm.value.prompt;
      console.log('Prompt submitted:', prompt);
      this.snackBar.open(`Prompt added: ${prompt}`, null, { duration: 2000 });
      this.addPromptForm.reset();
    } else {
      this.snackBar.open('Please correct the errors in the form', 'OK', { duration: 5000 });
    }
  }
}
