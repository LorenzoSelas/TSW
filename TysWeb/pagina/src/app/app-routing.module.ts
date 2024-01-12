import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RayaComponent } from './raya/raya.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { JuegosComponent } from './juegos/juegos.component';
import { UnoComponent } from './uno/uno.component';

const routes: Routes = [
  { path: 'Juegos', component: JuegosComponent},
  { path: 'Register', component: RegisterComponent},
  { path: 'Login', component: LoginComponent},
  { path: 'Tablero4r/:id', component: RayaComponent},
  { path: 'Uno/:id', component: UnoComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
