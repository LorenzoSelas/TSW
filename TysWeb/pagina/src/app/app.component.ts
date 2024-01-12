import { Component, ElementRef } from '@angular/core';
import { UsersService } from './users.service';
import { MatMenuTrigger } from '@angular/material/menu';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  userName = UsersService;
  socket = new WebSocket("ws://127.0.0.1:8080/wsUsuarios");
  title = 'juegos';
  position?: GeolocationPosition
  TemperaturaMax?: number
  TemperaturaMin?: number
  ciudad?: String

  constructor(private userService: UsersService, private el: ElementRef) {
    this.userService = userService;
    this.socket.onopen = function(event){
      console.log("Conexión establecida", event);
    }
    this.socket.onmessage = function(event){
      const chatBox = document.getElementById("chat-box") as HTMLDivElement;
      const message = document.createElement("p");
      message.textContent = event.data;
      chatBox.appendChild(message);
    }
    this.socket.onclose = function(){
      console.log("Conexión perdida.")
    }

    this.socket.addEventListener("close", (event: CloseEvent) => {
      console.log("WebSocket connection closed:", event);
    });

    navigator.geolocation.getCurrentPosition(
      position => {
        this.position = position
        console.log(position)
        console.log("latitud: " + position.coords.latitude + " logitud " + position.coords.longitude)

        this.obtenerElTiempo();
        this.obtenerCiudad();
      }
      , error => { console.log("error") }
    )

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

  private obtenerElTiempo() {
    let self = this
    let url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" + this.position?.coords.latitude + "%2C%20" + this.position?.coords.longitude + "?unitGroup=metric&include=days%2Ccurrent%2Chours%2Calerts&key=KXU7XHBR76JNAVP8YJ33NQ938&contentType=json"
    let req = new XMLHttpRequest();
    req.onreadystatechange = function () {
      if (this.readyState == this.DONE) {
        if (this.status >= 200 && this.status < 400) {
          let response = req.response
          response = JSON.parse(response)
          self.TemperaturaMax = response.days[0].tempmax
          self.TemperaturaMin = response.days[0].tempmin
        } else {
          console.log("error en la petición")
        }
      }
    }
    req.open("GET", url)
    req.send()
  }
  private obtenerCiudad() {
    let self = this
    let url = "https://nominatim.openstreetmap.org/reverse?lat=" + this.position?.coords.latitude + "&lon=" + this.position?.coords.longitude + "&format=json"
    let req = new XMLHttpRequest();
    req.onreadystatechange = function () {
      if (this.readyState == this.DONE) {
        if (this.status >= 200 && this.status < 400) {
          let response = req.response
          response = JSON.parse(response)
          self.ciudad = response.address.city
          console.log(self.ciudad)
        } else {
          console.log("error en la petición")
        }
      }
    }
    req.open("GET", url)
    req.send()
  }

  sendMessage() {
    const mensajeElement: HTMLInputElement = this.el.nativeElement.querySelector('#mensaje');
    const destinatarioElement: HTMLInputElement = this.el.nativeElement.querySelector('#destinatario');

    // Accede a los valores de los elementos
    const mensaje: string = mensajeElement.value;
    const destinatario: string = destinatarioElement.value;

    // Haz lo que necesites con los valores de los campos
    console.log('Mensaje:', mensaje);
    console.log('Destinatario:', destinatario);
    let msg = {
      tipo : "MENSAJE PRIVADO",
      destinatario : destinatario,
      texto : mensaje
    }
    this.socket.send(JSON.stringify(msg))
  }
}
/*userName = UserSService;

pwd = "1234";
ws? = WebSocket;
constructor(private userService: UserSService){
  this.userService = userService;*/


/*login(){
  let info= {
    email: this.userName,
    pwd: this.pwd
  }
  this.userService.login(info).subscribe(
    result=> {
      //this.ws = new WebSocket("ws://localhost:8080/wsGames?httpId=" + result.httpId)
    },
    Error =>
      alert(Error)
  )
}
}*/
