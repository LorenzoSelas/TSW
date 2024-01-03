import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagoSeguroComponent } from './pago-seguro.component';

describe('PagoSeguroComponent', () => {
  let component: PagoSeguroComponent;
  let fixture: ComponentFixture<PagoSeguroComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PagoSeguroComponent]
    });
    fixture = TestBed.createComponent(PagoSeguroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
