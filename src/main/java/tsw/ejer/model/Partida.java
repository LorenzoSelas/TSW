package tsw.ejer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private JSONArray cartas;
    private int numeroCarta;
    private Random random;
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
                    pasarTurno(carta);
                } else {
                    throw new MovimientoIlegalException("Esta carta no se puede jugar");
                }
                break;
            case 'b':
                if (carta.getColor() == this.ultimaCarta.getColor() || carta.getTipo() == this.ultimaCarta.getTipo()){
                    this.ultimaCarta = carta;
                    pasarTurno(carta);
                    pasarTurno(carta);
                } else {
                    throw new MovimientoIlegalException("Esta carta no se puede jugar");
                }
                break;
            case 'r':
                if (carta.getColor() == this.ultimaCarta.getColor() || carta.getTipo() == this.ultimaCarta.getTipo()){
                    this.ultimaCarta = carta;
                    sentido = !this.sentido;
                    pasarTurno(carta);
                } else {
                    throw new MovimientoIlegalException("Esta carta no se puede jugar");
                }
                break;
            case 's':
                if(carta.getTipo() == this.ultimaCarta.getTipo() && carta.getCantidad() == this.ultimaCarta.getCantidad()){
                    this.ultimaCarta =carta;
                    pasarTurno(carta);
                }else{
                    throw new NotImplementedException("Esta carta no se puede jugar");
                }
                
            case 'c':
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
        if(carta.getCantidad()== 2){
            cartas.put(new Random().nextInt(0-9), carta);
        }
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
        //Ideas para generar cartas y guardarlas en un JSONArray
        for(int i =0; i<80;i++ ){
            //cartas.put(crearCarta(ultimaCarta.getNumero(generarNumero()), ultimaCarta.getColor(), ultimaCarta.getCantidad()));
        }
    }

    private int generarNumero(){
        int numero= random.nextInt(6);
        //numero = 0, carta numérica
        if(numero ==0){
            numero= random.nextInt(10);
        } else{
            numero= '\0';
        }
        return numero;
    }

    private static JSONArray crearCarta(int numero, char color, int tipo) {
        JSONArray carta = new JSONArray();
        carta.put(crearAtributo("Numero de carta", numero));
        carta.put(crearAtributo("Color", color));
        carta.put(crearAtributo("Tipo de carta", tipo));
        return carta;
    }

    // Método para crear un objeto JSON que representa un atributo de la carta
    private static JSONObject crearAtributo(String nombre, Object valor) {
        JSONObject atributo = new JSONObject();
        atributo.put(nombre, valor);
        return atributo;
    }
}
