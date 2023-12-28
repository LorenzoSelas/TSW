package tsw.ejer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import tsw.ejer.Excepcion.MovimientoIlegalException;
import tsw.ejer.Excepcion.NotImplementedException;
import java.util.Collections;

//import lombok.Data;

//@Data
public class Partida {
    
    private String id;
    private boolean sentido;
    private Carta ultimaCarta;
    private User jugadorConTurno;
    private List<User> users;
    List<Carta> mazo = new ArrayList<>();
    private List<Carta> jugador1 = new ArrayList<>();
    private List<Carta> jugador2 = new ArrayList<>();
    private Mesa mesa = new Mesa();
    
    public Partida(){
        id = UUID.randomUUID().toString();
        this.users = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    /*Este método pone las cartas sobre la mesa si se puede
    He modificado los char a String para mayor claridad con los nombres y tipos*/ 
    public void poner(Carta carta, String idUser) throws MovimientoIlegalException, NotImplementedException{
        if (!getJugadorConTurno().getId().equals(idUser))
            throw new MovimientoIlegalException("Al usuario con id " + idUser + " no le toca jugar");
        switch (carta.getTipo()) {
            case "numerica":
                if (carta.getColor() == this.ultimaCarta.getColor() || carta.getNumero() == this.ultimaCarta.getNumero()){
                    this.ultimaCarta = carta;
                    pasarTurno(carta);
                } else {
                    throw new MovimientoIlegalException("Esta carta no se puede jugar");
                }
                break;
            case "bloqueo":
                if (carta.getColor() == this.ultimaCarta.getColor() || carta.getTipo() == this.ultimaCarta.getTipo()){
                    this.ultimaCarta = carta;
                    pasarTurno(carta);
                    pasarTurno(carta);
                } else {
                    throw new MovimientoIlegalException("Esta carta no se puede jugar");
                }
                break;
            case "sentido":
                if (carta.getColor() == this.ultimaCarta.getColor() || carta.getTipo() == this.ultimaCarta.getTipo()){
                    this.ultimaCarta = carta;
                    sentido = !this.sentido;
                    pasarTurno(carta);
                } else {
                    throw new MovimientoIlegalException("Esta carta no se puede jugar");
                }
                break;
            case "sumar":
                if(carta.getTipo() == this.ultimaCarta.getTipo() && carta.getCantidad() == this.ultimaCarta.getCantidad()){
                    this.ultimaCarta =carta;
                    pasarTurno(carta);
                }else{
                    throw new NotImplementedException("Esta carta no se puede jugar");
                }
                
            case "cambia-color":
                throw new NotImplementedException("Esta funcionalidad aun no esta implementada");
            default:
                throw new MovimientoIlegalException("No se ha encontrado una carta valida");       
        }
    }

    private void pasarTurno(Carta carta) {
        int actual = users.indexOf(this.jugadorConTurno);
        if (sentido)
            if (actual + 1 == this.users.size())
                this.jugadorConTurno = this.users.get(0);
            else
                this.jugadorConTurno = this.users.get(actual + 1);
        else
            if (actual == 0)
                this.jugadorConTurno = this.users.get(this.users.size() - 1);
            else
                this.jugadorConTurno = this.users.get(actual - 1);
        
    }

    public List<User> getUsers(){
        return this.users;
    }

    public void addUser(User user){
        this.users.add(user);
    }
    public User getJugadorConTurno(){
        return this.jugadorConTurno;
    }

    public void iniciar(){
        this.jugadorConTurno = this.users.get(new Random().nextInt(this.users.size()));
        this.sentido = true;

        //Cartas numéricas: 72
        //Cartas numéricas del 0 al 9
        for (int i = 0; i <= 9; i++) {
            mazo.add(new Carta(i, "rojo"));
            mazo.add(new Carta(i, "amarillo"));
            mazo.add(new Carta(i, "verde"));
            mazo.add(new Carta(i, "azul"));
        }

        //Cartas numéricas del 1 al 9
        for (int i = 1; i <= 9; i++) {
            mazo.add(new Carta(i, "rojo"));
            mazo.add(new Carta(i, "amarillo"));
            mazo.add(new Carta(i, "verde"));
            mazo.add(new Carta(i, "azul"));
        }

        //Cartas de sumar 2, bloqueo, y cambiar turno: 24 en total
        for (int i = 0; i < 2; i++) {
            mazo.add(new Carta("+2", "rojo", 2));
            mazo.add(new Carta("+2", "amarillo", 2));
            mazo.add(new Carta("+2", "verde", 2));
            mazo.add(new Carta("+2", "azul", 2));

            mazo.add(new Carta("bloqueo", "rojo"));
            mazo.add(new Carta("bloqueo", "amarillo"));
            mazo.add(new Carta("bloqueo", "verde"));
            mazo.add(new Carta("bloqueo", "azul"));

            mazo.add(new Carta("cambiar-turno", "rojo"));
            mazo.add(new Carta("cambiar-turno", "amarillo"));
            mazo.add(new Carta("cambiar-turno", "verde"));
            mazo.add(new Carta("cambiar-turno", "azul"));
        }

        //Cartas de cambio de color y sumar 4 con cambio de color: 8 en total
        for (int i = 0; i < 4; i++) {
            mazo.add(new Carta("cambiar-color", 4));
            mazo.add(new Carta("+4", "cambiar-color", 4));
        }

        // Barajar el mazo 72+24+8= 104 cartas
        Collections.shuffle(mazo); 
        repartir(mazo);
    }

    //Reparte las cartas a los dos jugaores y pone una sobre la mesa
    public void repartir(List<Carta> mazo){
        for (int i = 0; i < 7; i++) {
            jugador1.add(mazo.remove(0));
            jugador2.add(mazo.remove(0));
        }
        // Poner una carta sobre la mesa
        mesa.setCarta(mazo.remove(0));
    }

    
}
