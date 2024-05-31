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
    this.router.navigate([this.juego]);
    this.actualizarLista();
  }

  entrar(id: string){
    this.http.get<any>("http://localhost:8080/matches/entrar?id=" + id, { withCredentials : true}).subscribe( 
    (data) => {
      this.manager.idPartida = id;
      this.router.navigate([this.juego +"/"+ data.id]);
      console.log(data.id);
    },
    (error) => {
      console.error('Error al entrar en la sala:', error);
    }
  ); 
  }
  Nueva() {
    this.http.get<any>("http://localhost:8080/matches/start?tipo=" + this.juego, { withCredentials : true}).subscribe( 
    (data) => {
      this.manager.idPartida = data.id;
      this.router.navigate([this.juego +"/"+ data.id]);
      console.log(data.id);
    },
    (error) => {
      console.error('Error al obtener las salas:', error);
    }
  ); 
   
  
  }
  BorrarPartidas(){
    
  }
  actualizarLista() {
    this.http.get("http://localhost:8080/matches/" + this.juego + "/ids", { withCredentials : true}).subscribe( 
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

