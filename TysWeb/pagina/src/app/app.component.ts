import { Component } from '@angular/core';
import { UsersService } from './users.service';
import { MatMenuTrigger } from '@angular/material/menu';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'juegos';
  position?: GeolocationPosition
  TemperaturaMax?: number
  TemperaturaMin?: number
  ciudad?: String

  constructor() {
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
