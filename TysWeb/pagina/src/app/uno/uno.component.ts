import { MatList } from '@angular/material/list';
import { uno } from './uno';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { UnoService } from './uno.service';
import { Component, ViewChild, ElementRef } from '@angular/core';
import { ManagerService } from '../manager.service';
import { Carta } from './uno.model';
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

  ngOnInit(): void {
    const idUsuario = '1'; // Aquí debes obtener el ID del usuario actual
    const idTablero = '1'; // Aquí debes obtener el ID del tablero actual
    this.repartirCartas(idUsuario, idTablero);
}
  iniciarPartida() {
    this.mostrarElemento = true;
  }

  repartirCartas(idUsuario: string, idTablero: string) {
    this.unoService.repartirCartas(idUsuario, idTablero).subscribe(
        () => {
            console.log()
        },
        error => {
            console.log('Error al repartir cartas:', error);
        }
    );
}
  ponerCasilla(columna: number) {
    this.unoService.ponerCarta(this.manager.idPartida!).subscribe(
      _response => {
        console.log("Se ha puesto una carta");
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
