import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { client } from 'websocket';

@Component({
  selector: 'app-juegos',
  templateUrl: './juegos.component.html',
  styleUrls: ['./juegos.component.css']
})

export class JuegosComponent {
Nueva() {
  this.http.get("http://localhost:8080/matches/start?tipo=" + this.juego).subscribe( 
  (data) => {
    this.listaDeJuegos = data;
  },
  (error) => {
    console.error('Error al obtener las salas:', error);
  }
);
}
  juego: string = ''
  listaDeJuegos: any = [];

  constructor(private http: HttpClient) { }

  setearJuego(juego: string) {
    this.juego = juego;
    this.actualizarLista();
  }

  actualizarLista() {
    this.http.get("http://localhost:8080/matches/" + this.juego + "/ids").subscribe( 
      (data) => {
        this.listaDeJuegos = data;
      },
      (error) => {
        console.error('Error al obtener las salas:', error);
      }
    ); //peticion http para recoger las salas abiertas 
  }
}

