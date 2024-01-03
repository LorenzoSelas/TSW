import { Component } from '@angular/core';
import { user } from '../user/user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm : FormGroup;
  usuario: user;
  respuestaOK: boolean;

  constructor(private usersService: UsersService, private formBuilder:FormBuilder){
    this.registerForm =  this.formBuilder.group({
      Nombre : ['', [Validators.required,Validators.minLength(3)]],
      Email : ['', [Validators.required,Validators.email]],
      Pwd1 : ['',[Validators.required,Validators.minLength(5)]],
      Pwd2 : ['',[Validators.required,Validators.minLength(5)]]
  },);
    this.usuario = new user;
    this.respuestaOK = false;
  }

  onSubmit() {
    this.respuestaOK = false;

    this.usuario.datosRegistro(this.registerForm.controls['Nombre'].value, this.registerForm.controls['Email'].value, this.registerForm.controls['Pwd1'].value, this.registerForm.controls['Pwd2'].value);
    this.usersService.registrarUsuario(this.usuario).subscribe((data) => {
      console.log(JSON.stringify(data))
      this.respuestaOK = true
    })

  }

  bindeo() {
    console.log(this.registerForm.value);
  }

  getNombreErrorMessage(Control: string) {
    const nombreControl = this.registerForm.get(Control);
  
    if (nombreControl?.hasError('required')) {
      return 'El campo nombre es obligatorio';
    }
  
    if (nombreControl?.hasError('minlength')) {
      return 'El nombre debe tener mínimo 3v caracteres';
    }
  
    return '';
  }

  getEmailErrorMessage(Control:string) {
    const emailControl = this.registerForm.get(Control);
  
    if (emailControl?.hasError('required')) {
      return 'El campo email es obligatorio';
    }
  
    if (emailControl?.hasError('email')) {
      return 'El Email debe tener un formato de email';
    }
  
    return '';
  }
  
  getPasswordErrorMessage(Control: string) {
    const passwordControl = this.registerForm.get(Control);
  
    if (passwordControl?.hasError('required')) {
      return 'El campo contraseña es obligatorio';
    }
  
    if (passwordControl?.hasError('minlength')) {
      return 'La contraseña debe tener mínimo 5 caracteres';
    }
  
    return '';
  }
  
}
