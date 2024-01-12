import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { user } from '../user/user';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent {
  usuario: user;
  loginForm: FormGroup;
  ws? = WebSocket;

  constructor(private usersService: UsersService, private formBuilder: FormBuilder) {
    this.loginForm = this.formBuilder.group({
      Email: ['', [Validators.required, Validators.email]],
      Pwd: ['', [Validators.required, Validators.minLength(5)]]
    },);
    this.usuario = new user;
  }

  onSubmit() {
    this.usuario.datosLogin(this.loginForm.get('Email')?.value, this.loginForm.get('Pwd')?.value);
    this.usersService.login(this.usuario).subscribe((data) => {
      console.log(JSON.stringify(data));
    })
  }

  getEmailErrorMessage() {
    const emailControl = this.loginForm.get('Email');

    if (emailControl?.hasError('required')) {
      return 'El campo email es obligatorio';
    }

    if (emailControl?.hasError('email')) {
      return 'El Email debe tener un formato de email';
    }

    return '';
  }

  getPasswordErrorMessage() {
    const passwordControl = this.loginForm.get('Pwd');

    if (passwordControl?.hasError('required')) {
      return 'El campo contraseña es obligatorio';
    }

    if (passwordControl?.hasError('minlength')) {
      return 'La contraseña debe tener mínimo 5 caracteres';
    }

    return '';
  }

}

