import { Component } from '@angular/core';
import { MatList } from '@angular/material/list';
import { raya } from './raya';

@Component({
  selector: 'app-raya',
  templateUrl: './raya.component.html',
  styleUrls: ['./raya.component.css']
})

export class RayaComponent {
  partida:raya
  poner:Boolean
  constructor(){
    this.partida= new raya
    this.poner = new Boolean
  }
  ocuparCelda(row: number,col: number){
    if(this.puedoPoner()){
      this.partida.celdas[row][col] = 'X'
    }else{

    }
  }
  puedoPoner(): boolean{
    return true
  }
}
