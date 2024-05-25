import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { client } from 'websocket';
import { Router } from '@angular/router';
import { ManagerService } from '../manager.service';

@Component({
  selector: 'app-juegos',
  templateUrl: './juegos.component.html',
  styleUrls: ['./juegos.component.css']
})

export class JuegosComponent {

  juego: string = ''
  listaDeJuegos: any = [];
  idPartida: string = '';

  constructor(private http: HttpClient, private router: Router, private manager: ManagerService) {

  }

  setearJuego(juego: string) {
    this.juego = juego;
    this.actualizarLista();
    this.router.navigate([juego]);
  }
  Nueva() {
    this.http.get<any>("http://localhost:8080/matches/start?tipo=" + this.juego).subscribe( 
    (data) => {
      this.listaDeJuegos = data;
      this.manager.idPartida = data.id;
      this.actualizarLista();
      console.log(this.juego);
    },
    (error) => {
      console.error('Error al obtener las salas:', error);
    }
  ); 
   
  
  }
  BorrarPartidas(){
    
  }
  actualizarLista() {
    this.http.get("http://localhost:8080/matches/" + this.juego + "/ids").subscribe( 
      (data) => {
        this.listaDeJuegos = data;
        console.log(this.juego, data )
      },
      (error) => {
        console.error('Error al obtener las salas:', error);
      }
    ); //peticion http para recoger las salas abiertas 
  }
}

