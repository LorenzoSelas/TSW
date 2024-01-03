import { Component, OnInit } from '@angular/core';
import { PaymentsService } from './pagoServicio';
@Component({
  selector: 'app-payments',
  templateUrl: './pago-seguro.component.html',
  styleUrls: ['./pago-seguro.component.css']
})
export class PaymentsComponent implements OnInit {
  amount: number = 10
  constructor(private paymentsService: PaymentsService) { }
  ngOnInit(): void { }
  requestPrepayment() {
    this.paymentsService.prepay(this.amount).subscribe({
      next: (response: any) => {
        alert(response.body)
      },
      error: (response: any) => {
        alert(response)
      }
    })
  }
}