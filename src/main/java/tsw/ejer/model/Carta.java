package tsw.ejer.model;

public class Carta {
    private int numero;
    private char tipo;
    private char color;
    private int cantidad;

    public Carta(int n, char c){
        this.tipo = 'n';
        this.numero = n;
        this.color = c;
    }

    public Carta(char t, char c){
        this.tipo = t;
        this.color = c;
    }

    public Carta(char t, char c, int ca){
        this.tipo = t;
        this.color = c;
        this.cantidad = ca;
    }

    public int getNumero(){return this.numero;}
    public char getColor(){return this.color;}
    public char getTipo(){return this.tipo;}
    public int getCantidad(){return this.cantidad;}
}
