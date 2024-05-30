import { MatList } from '@angular/material/list';
import { uno } from './uno';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { UnoService } from './uno.service';
import { Component, ViewChild, ElementRef } from '@angular/core';
import { ManagerService } from '../manager.service';
@Component({
  selector: 'app-uno',
  templateUrl: './uno.component.html',
  styleUrls: ['./uno.component.css']
})
export class UnoComponent {
  id: string = '';
  tablero: string = '';
  partida: uno;
  poner: Boolean;
  mostrarElemento: boolean = false;
  constructor(private http: HttpClient, private route: ActivatedRoute, private unoService: UnoService, private manager: ManagerService) {
    this.partida = new uno();
    this.poner = false;
  }

  iniciar() {
    this.mostrarElemento = !this.mostrarElemento;
  }
  ponerCasilla(columna: number) {
    this.unoService.ponerCasilla(this.manager.idPartida!, columna).subscribe(
      _response => {
        console.log("Se ha puesto una casilla");
        this.ocuparCelda(_response.casillas);
      },
      _error => {
        console.log("Error al realizar la petición al servidor");
      }
    );
  }
  ocuparCelda(casillas: string[]) {
    // Actualiza las celdas de la partida con las casillas recibidas
    for (let i = 0; i < casillas.length; i++) {
      for (let j = 0; j < casillas[i].length; j++) {
        if (casillas[i][j] == "\0"){
          this.partida.celdas1[i][j] = "";
        } else {
          this.partida.celdas1[i][j] = casillas[i][j];
        }
      }
    }
  
    // Imprime las celdas en la consola para depuración
    for (let i = 0; i < this.partida.celdas1.length; i++) {
      console.log(`Fila ${i}: ${this.partida.celdas1[i]}`);
    }
  }

}
