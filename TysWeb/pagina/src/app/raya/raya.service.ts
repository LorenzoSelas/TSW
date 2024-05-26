import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ManagerService } from '../manager.service';

@Injectable({
  providedIn: 'root'
})
export class RayaService {
  constructor(private http: HttpClient, private manager: ManagerService) { }

  ponerCasilla(idTablero: string, columna: number) {
    const idUsuario =this.http.get<any>('http://localhost:8080/users/usuario'); 
    console.log(idUsuario);
  
    // Crear el cuerpo de la solicitud que incluya el id del usuario, el id del tablero y la columna
    const body = { userId: idUsuario, column: columna };
  
    // Enviar la solicitud POST al servidor
    return this.http.post<any>('http://localhost:8080/matches/poner/'+idTablero, body, { withCredentials : true});
  }
  puedoPoner(){
    // Enviar la solicitud POST al servidor
    return this.http.get<any>('http://localhost:8080/matches/meToca/', { withCredentials : true});
  }
}
