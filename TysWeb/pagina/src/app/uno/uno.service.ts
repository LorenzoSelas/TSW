import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ManagerService } from '../manager.service';
import { Carta } from './uno.model';
import { uno } from './uno';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class UnoService {
    constructor(private http: HttpClient, private manager: ManagerService) { }

    repartirCartas(idUsuario: string, idTablero: string): Observable<uno[]> {
        const params = { tipo: 'tipo_del_tablero' }; // Aquí debes reemplazar 'tipo_del_tablero' con el tipo adecuado
        return this.http.get<any>('http://localhost:8080/matches/start', { params });
    }

    ponerCarta(idTablero: string) {
        const idUsuario = this.http.get<any>('http://localhost:8080/users/usuario');
        console.log(idUsuario);

        // Crear el cuerpo de la solicitud que incluya el id del usuario, el id del tablero y la columna
        const body = {};

        // Enviar la solicitud POST al servidor
        return this.http.post<any>('http://localhost:8080/matches/poner/' + idTablero, body, { withCredentials: true });
    }

    puedoPoner(idTablero: string) {
        // Enviar la solicitud POST al servidor
        return this.http.get<any>('http://localhost:8080/matches/meToca/' + idTablero, { withCredentials: true });
    }
}
