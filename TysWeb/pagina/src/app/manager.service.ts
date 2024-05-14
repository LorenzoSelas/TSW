import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {
  idPartida?: string;
  idUser?: string;
  
  constructor() { }
}
