
import { MatList } from '@angular/material/list';
import { raya } from './raya';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Component, ViewChild, ElementRef } from '@angular/core';
import { RayaService } from './raya.service';
import { ManagerService } from '../manager.service';

@Component({
  selector: 'app-raya',
  templateUrl: './raya.component.html',
  styleUrls: ['./raya.component.css']
})

export class RayaComponent {
  id: string = '';
  tablero: string = '';
  partida: raya;
  poner: Boolean;
  mostrarElemento: boolean = false;

  constructor(private http: HttpClient, private route: ActivatedRoute, private rayaService: RayaService, private manager: ManagerService) {
    this.partida = new raya;
    this.poner = new Boolean;
  }

  ngOnInit() {
    // Accede al id desde la ruta actual
    this.route.paramMap.subscribe((params: { get: (arg0: string) => any; }) => {
      const id = params.get('id');
      console.log('ID:', id);
      this.manager.idPartida = id;
      this.manager.idUser = id;
    });
  }

  iniciar() {
    this.mostrarElemento = !this.mostrarElemento;
  }
  ponerCasilla(columna: number) {
    this.rayaService.ponerCasilla(this.manager.idPartida!, columna).subscribe(
      _response => {
        console.log("Se ha puesto una casilla");
        this.ocuparCelda(_response.casillas);
      },
      _error => {
        console.log("Error al realizar la petición al servidor")
      }
    );
  }
  ocuparCelda(casillas: string[]) {
    // Actualiza las celdas de la partida con las casillas recibidas
    for (let i = 0; i < casillas.length; i++) {
      for (let j = 0; j < casillas[i].length; j++) {
        this.partida.celdas[i][j] = casillas[i][j];
      }
    }
  
    // Imprime las celdas en la consola para depuración
    for (let i = 0; i < this.partida.celdas.length; i++) {
      console.log(`Fila ${i}: ${this.partida.celdas[i].join(' ')}`);
    }
  }
  puedoPoner(): boolean {
    return true
  }
}
