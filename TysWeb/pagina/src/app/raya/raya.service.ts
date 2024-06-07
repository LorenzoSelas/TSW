import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ManagerService } from '../manager.service';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RayaService {
  private wsroom: WebSocket | undefined;
  public tablero: string[][] = [];  // Variable para almacenar el tablero
  public tableroSubject = new Subject<string[][]>();  // Subject para emitir eventos del tablero

  constructor(private http: HttpClient, private manager: ManagerService) { }

  inicializar() {
    this.wsroom = new WebSocket("ws://localhost:8080/wsGames/" + this.manager.idPartida);

    // Manejar eventos de WebSocket
    this.wsroom.onopen = () => {
      console.log('Conectado sala:' + this.manager.idPartida);
      // Aquí puedes emitir eventos o realizar acciones adicionales cuando se abre la conexión
    };

    this.wsroom.onmessage = (event) => {
      console.log('Mensaje recibido en:' + this.manager.idPartida, event.data);
      this.procesarMensaje(event.data);
    };

    this.wsroom.onclose = () => {
      console.log('Desconectado');
      // Aquí puedes realizar acciones adicionales cuando se cierra la conexión
    };
  }

  private procesarMensaje(data: string) {
    try {
      // Si el mensaje empieza con "TABLERO:", procésalo de manera especial
      if (data.startsWith("TABLERO:")) {
        const tableroString = data.substring(8);  // Eliminar "TABLERO:"
        const tableroArray = JSON.parse(tableroString);
        this.tablero = tableroArray;
        console.log('Tablero actualizado:', this.tablero);
        this.tableroSubject.next(this.tablero);  // Emitir evento del tablero actualizado
      } else {
        console.warn('Formato de mensaje no esperado:', data);
      }
    } catch (error) {
      console.error('Error procesando el mensaje:', error);
    }
  }

  ponerCasilla(idTablero: string, columna: number) {
    // Crear el cuerpo de la solicitud que incluya el id del usuario, el id del tablero y la columna
    const body = { column: columna };

    // Enviar la solicitud POST al servidor
    return this.http.post<any>('http://localhost:8080/matches/poner/' + idTablero, body, { withCredentials: true });
  }

  puedoPoner() {
    // Enviar la solicitud POST al servidor
    return this.http.get<any>('http://localhost:8080/matches/meToca/', { withCredentials: true });
  }
}
