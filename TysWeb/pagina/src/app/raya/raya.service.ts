import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RayaService {
  constructor(private http: HttpClient) { }

  ponerCasilla(idTablero: string, columna: number) {
    // Obtener el id del usuario de alguna manera, por ejemplo, desde el servicio de autenticación
    const idUsuario = 'fd19c311-4c37-4f09-9a3d-e575dd85807c'; // Aquí obtén el id del usuario de alguna manera
  
    // Crear el cuerpo de la solicitud que incluya el id del usuario, el id del tablero y la columna
    const body = { userId: idUsuario, column: columna };
  
    // Enviar la solicitud POST al servidor
    return this.http.post<any>('http://localhost:8080/matches/poner/'+idTablero, body, { withCredentials : true});
  }
}
