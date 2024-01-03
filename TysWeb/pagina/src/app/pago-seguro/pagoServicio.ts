import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
//import { httpUrl } from '../commons';

@Injectable({
    providedIn: 'root'
})
export class PaymentsService {

    constructor(private client: HttpClient) { }

    prepay(matches: number): Observable<Object> {
        return this.client.get("http://localhost:8080/" + "payments/prepay?matches=" + matches, {
            withCredentials: true,
            observe: "response",
            responseType: "text"
        })
    }
}