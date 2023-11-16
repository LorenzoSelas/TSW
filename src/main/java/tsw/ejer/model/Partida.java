package tsw.ejer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import tsw.ejer.Excepcion.MovimientoIlegalException;
import tsw.ejer.Excepcion.NotImplementedException;

//import lombok.Data;

//@Data
public class Partida {
    private String id;
    private boolean sentido;
    private Carta ultimaCarta;
    private User jugadorConTurno;
    private List<User> users;

    public Partida(){
        id = UUID.randomUUID().toString();
        this.users = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    /*
     * Metodo para poner carta sobre la ultima carta
     * 
     * Parametros:
     *  carta: carta que queramos jugar
     *      n: numerica
     *      b: bloqueo
     *      r: revertir
     *      s: sumar
     *      c: cambiar color
     *  idUser: usuario que intenta jugar
     */
    public void poner(Carta carta, String idUser) throws MovimientoIlegalException, NotImplementedException{
        if (!getJugadorConTurno().getId().equals(idUser))
            throw new MovimientoIlegalException("Al usuario con id " + idUser + " no le toca jugar");
        switch (carta.getTipo()) {
            case 'n':
                if (carta.getColor() == this.ultimaCarta.getColor() || carta.getNumero() == this.ultimaCarta.getNumero()){
                    this.ultimaCarta = carta;
                    pasarTurno();
                } else {
                    throw new MovimientoIlegalException("Esta carta no se puede jugar");
                }
                break;
            case 'b':
                if (carta.getColor() == this.ultimaCarta.getColor() || carta.getTipo() == this.ultimaCarta.getTipo()){
                    this.ultimaCarta = carta;
                    pasarTurno();
                    pasarTurno();
                } else {
                    throw new MovimientoIlegalException("Esta carta no se puede jugar");
                }
                break;
            case 'r':
                if (carta.getColor() == this.ultimaCarta.getColor() || carta.getTipo() == this.ultimaCarta.getTipo()){
                    this.ultimaCarta = carta;
                    sentido = !this.sentido;
                    pasarTurno();
                } else {
                    throw new MovimientoIlegalException("Esta carta no se puede jugar");
                }
                break;
            case 's':
                throw new NotImplementedException("Esta funcionalidad aun no esta implementada");
            case 'c':
                throw new NotImplementedException("Esta funcionalidad aun no esta implementada");
            default:
                throw new MovimientoIlegalException("No se ha encontrado una carta valida");       
        }
    }

    private void pasarTurno() {
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
        /*
         * repartir cartas de un posible json
         */
    }
}
