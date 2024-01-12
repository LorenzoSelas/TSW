import { Component, Inject } from '@angular/core';
import { MatList } from '@angular/material/list';
import { raya } from './raya';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-raya',
  templateUrl: './raya.component.html',
  styleUrls: ['./raya.component.css']
})

export class RayaComponent {
  id: string = '';
  partida:raya;
  poner:Boolean;

  constructor(private http: HttpClient, private route: ActivatedRoute){
    this.partida = new raya;
    this.poner = new Boolean;
  }

  ngOnInit() {
    // Accede al id desde la ruta actual
    this.route.paramMap.subscribe((params: { get: (arg0: string) => any; }) => {
      const id = params.get('id');
      console.log('ID:', id);
      // Haz lo que necesites con el id aquí
    });
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
