// src/app/services/websocket.service.ts
import { Injectable } from '@angular/core';
import { WebSocketSubject, webSocket } from 'rxjs/webSocket';
import { Observable } from 'rxjs';

interface Message {
  tipo: string;
  [key: string]: any;
}

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private ws: WebSocket | undefined;
  private wsroom: WebSocket | undefined;
  conectar(nombre: string) {
    // Inicializar la conexión WebSocket
    this.ws = new WebSocket("ws://localhost:8080/wsUsuarios?nombre=" + encodeURI(nombre));

    // Manejar eventos de WebSocket
    this.ws.onopen = () => {
      console.log('Conectado');
      // Aquí puedes emitir eventos o realizar acciones adicionales cuando se abre la conexión
    };

    this.ws.onmessage = (event) => {
      console.log('Mensaje recibido:', event.data);
      // Aquí puedes procesar los mensajes recibidos del servidor WebSocket
    };

    this.ws.onclose = () => {
      console.log('Desconectado');
      // Aquí puedes realizar acciones adicionales cuando se cierra la conexión
    };
  }

  entrarSala(id: string){
    this.wsroom = new WebSocket("ws://localhost:8080/wsGames/" + id);

    // Manejar eventos de WebSocket
    this.wsroom.onopen = () => {
      console.log('Conectado sala:' + id);
      // Aquí puedes emitir eventos o realizar acciones adicionales cuando se abre la conexión
    };

    this.wsroom.onmessage = (event) => {
      console.log('Mensaje recibido en:' + id, event.data);
      // Aquí puedes procesar los mensajes recibidos del servidor WebSocket
    };

    this.wsroom.onclose = () => {
      console.log('Desconectado');
      // Aquí puedes realizar acciones adicionales cuando se cierra la conexión
    };
  }

  constructor() {
  }
}
