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
  conectar() {
    // Inicializar la conexión WebSocket
    this.ws = new WebSocket("ws://localhost:8080/wsGames");

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

  private socket$: WebSocketSubject<Message>;
  public messages$: Observable<Message>;

  constructor() {
    this.socket$ = webSocket('ws://127.0.0.1:8080/wsUsuarios');
    this.messages$ = this.socket$.asObservable();
  }

  sendMessage(msg: Message) {
    this.socket$.next(msg);
  }
}
