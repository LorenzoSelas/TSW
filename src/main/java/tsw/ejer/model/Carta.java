package tsw.ejer.model;

public class Carta {
    private int numero;
    private String tipo;
    private String color;
    private int cantidad;

    //cartas numéricas
    public Carta(int numero, String color){
        this.tipo = "numerica";
        this.numero = numero;
        this.color = color;
    }

    //cartas bloqueo y revertir
    public Carta(String tipo, String color){
        this.tipo = tipo;
        this.color = color;
    }

    //cartas +2 y +4
    public Carta(String tipo, String color, int cantidad){
        this.tipo = tipo;
        this.color = color;
        this.cantidad = cantidad;
    }

    //cartas cambio de color
    public Carta(String tipo, int cantidad){
        this.tipo = "cambiar-color";
    }

    public String getInfo() {
        return "Número: " + numero +
                ", Tipo: " + tipo +
                ", Color: " + color +
                ", Cantidad: " + cantidad;
    }
    
    public int getNumero(){return this.numero;}
    public String getColor(){return this.color;}
    public String getTipo(){return this.tipo;}
    public int getCantidad(){return this.cantidad;}
}
