// src/app/app.component.ts
import { Component, ElementRef,ViewChild, OnInit, OnDestroy } from '@angular/core';
import { UsersService } from './users.service';
import { MatMenuTrigger } from '@angular/material/menu';
import { Router } from '@angular/router'; 
import { WebSocketService } from './services/websocket.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  usuarioInput!: ElementRef;
  estadoSpan!: ElementRef;
  zonaDeChat!: ElementRef;
  destinatarioInput!: ElementRef;
  mensajeInput!: ElementRef;

  userName = UsersService;
  title = 'juegos';
  position?: GeolocationPosition;
  TemperaturaMax?: number;
  TemperaturaMin?: number;
  ciudad?: String;
  estado: string = 'Desconectado';
  private wsSubscription: Subscription | undefined;
  
  constructor(
    private userService: UsersService,
    private el: ElementRef,
    private router: Router,
    private wsService: WebSocketService,
    
  ) {
    this.userService = userService;

    navigator.geolocation.getCurrentPosition(
      position => {
        this.position = position;
        console.log(position);
        console.log("latitud: " + position.coords.latitude + " longitud " + position.coords.longitude);

        this.obtenerElTiempo();
        this.obtenerCiudad();
      },
      error => { console.log("error") }
    );
  }
  
  ngOnInit() {
    this.wsSubscription = this.wsService.messages$.subscribe(message => {
      const chatBox = document.getElementById("chat-box") as HTMLDivElement;
      const messageElement = document.createElement("p");
      messageElement.textContent = JSON.stringify(message);
      chatBox.appendChild(messageElement);

      if (message.tipo === "NUEVO USUARIO") {
        // Handle new user logic here
      } else if (message.tipo === "MENSAJE PRIVADO") {
        // Handle private message logic here
      } else if (message.tipo === "BIENVENIDA") {
        // Handle welcome message logic here
      }
    });
  }
  conectar() {
    this.wsService.conectar(); 
    this.estado = 'Conectando...';
    console.log("Conectado");
  }
  enviar() {
    let msg = {
      tipo: "MENSAJE PRIVADO",
      destinatario: this.destinatarioInput.nativeElement.value,
      texto: this.mensajeInput.nativeElement.value
    };
    this.wsService.sendMessage(msg);
  }
  ngOnDestroy() {
    if (this.wsSubscription) {
      this.wsSubscription.unsubscribe();
    }
  }

  openMyMenu(menuTrigger: MatMenuTrigger) {
    if (!menuTrigger.menuOpen) {
      menuTrigger.openMenu();
    }
  }

  closeMyMenu(menuTrigger: MatMenuTrigger) {
    if (menuTrigger.menuOpen) {
      menuTrigger.closeMenu();
    }
  }
  
  sendMessage() {
    const mensajeElement: HTMLInputElement = this.el.nativeElement.querySelector('#mensaje');
    const destinatarioElement: HTMLInputElement = this.el.nativeElement.querySelector('#destinatario');

    const mensaje: string = mensajeElement.value;
    const destinatario: string = destinatarioElement.value;

    console.log('Mensaje:', mensaje);
    console.log('Destinatario:', destinatario);
    
    let msg = {
      tipo : "MENSAJE PRIVADO",
      destinatario : destinatario,
      texto : mensaje
    };
    this.wsService.sendMessage(msg);
  }

  navigateToJuegos() {
    this.router.navigate(['/juegos']);
  }

  isHomeRoute(): boolean {
    return this.router.url === '/';
  }

  private obtenerElTiempo() {
    const url = `https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/${this.position?.coords.latitude}%2C%20${this.position?.coords.longitude}?unitGroup=metric&include=days%2Ccurrent%2Chours%2Calerts&key=KXU7XHBR76JNAVP8YJ33NQ938&contentType=json`;
    const req = new XMLHttpRequest();
    req.onreadystatechange = () => {
      if (req.readyState == XMLHttpRequest.DONE) {
        if (req.status >= 200 && req.status < 400) {
          const response = JSON.parse(req.responseText);
          this.TemperaturaMax = response.days[0].tempmax;
          this.TemperaturaMin = response.days[0].tempmin;
        } else {
          console.log("error en la petición");
        }
      }
    };
    req.open("GET", url);
    req.send();
  }

  private obtenerCiudad() {
    const url = `https://nominatim.openstreetmap.org/reverse?lat=${this.position?.coords.latitude}&lon=${this.position?.coords.longitude}&format=json`;
    const req = new XMLHttpRequest();
    req.onreadystatechange = () => {
      if (req.readyState == XMLHttpRequest.DONE) {
        if (req.status >= 200 && req.status < 400) {
          const response = JSON.parse(req.responseText);
          this.ciudad = response.address.city;
          console.log(this.ciudad);
        } else {
          console.log("error en la petición");
        }
      }
    };
    req.open("GET", url);
    req.send();
  }
}
