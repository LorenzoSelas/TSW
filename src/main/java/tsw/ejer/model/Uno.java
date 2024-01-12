package tsw.ejer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import tsw.ejer.Excepcion.MovimientoIlegalException;
import tsw.ejer.Excepcion.NotImplementedException;
import java.util.Collections;

//import lombok.Data;

//@Data
public class Uno extends Tablero {

    private boolean sentido;
    private Carta ultimaCarta;
    private List<Carta> mazo = new ArrayList<>();
    private List<List<Carta>> cartasJugadores;

    public Uno() {
        this.cartasJugadores = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    private void pasarTurno(Carta carta) {
        int actual = users.indexOf(this.jugadorConTurno);
        if (sentido)
            if (actual + 1 == this.users.size())
                this.jugadorConTurno = this.users.get(0);
            else
                this.jugadorConTurno = this.users.get(actual + 1);
        else if (actual == 0)
            this.jugadorConTurno = this.users.get(this.users.size() - 1);
        else
            this.jugadorConTurno = this.users.get(actual - 1);

    }

    private boolean tieneCartasParaSumar(User jugador, int cantidad) {
        return cartasJugadores.get(users.indexOf(jugador)).size() >= cantidad;
    }

    private void agregarCartasASiguienteJugador(User jugador, int cantidad) {
        List<Carta> cartasSiguienteJugador = cartasJugadores.get(users.indexOf(jugador));
        for (int i = 0; i < cantidad; i++) {
            Carta carta = mazo.remove(0); // Tomar la carta del mazo
            cartasSiguienteJugador.add(carta); // Agregar la carta al siguiente jugador
        }
    }

    private void cambiarUltimaCarta(Carta nuevaUltimaCarta) {
        this.ultimaCarta = nuevaUltimaCarta;
    }

    private void acumularCartasEnUltimaCarta(User siguienteJugador, int cantidad) {
        List<Carta> cartasSiguienteJugador = cartasJugadores.get(users.indexOf(siguienteJugador));
        Carta carta = null;
        for (int i = 0; i < cantidad; i++) {
            carta = cartasSiguienteJugador.remove(0); // Tomar la carta del siguiente jugador
            mazo.add(carta); // Agregar la carta al mazo
        }
        cambiarUltimaCarta(carta); // La última carta ahora es la carta que se tomó del siguiente jugador
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public User getJugadorConTurno() {
        return this.jugadorConTurno;
    }

    private void quitarCartaAJugador(Carta carta, User jugador) {
        cartasJugadores.get(users.indexOf(jugador)).remove(carta);
    }

    public void iniciar() {
        this.jugadorConTurno = this.users.get(new Random().nextInt(this.users.size()));
        this.sentido = true;

        // Cartas numéricas: 72
        // Cartas numéricas del 0 al 9
        for (int i = 0; i <= 9; i++) {
            mazo.add(new Carta(i, "rojo"));
            mazo.add(new Carta(i, "amarillo"));
            mazo.add(new Carta(i, "verde"));
            mazo.add(new Carta(i, "azul"));
        }

        // Cartas numéricas del 1 al 9
        for (int i = 1; i <= 9; i++) {
            mazo.add(new Carta(i, "rojo"));
            mazo.add(new Carta(i, "amarillo"));
            mazo.add(new Carta(i, "verde"));
            mazo.add(new Carta(i, "azul"));
        }

        // Cartas de sumar 2, bloqueo, y cambiar turno: 24 en total
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

        // Cartas de cambio de color y sumar 4 con cambio de color: 8 en total
        for (int i = 0; i < 4; i++) {
            mazo.add(new Carta("cambiar-color", 4));
            mazo.add(new Carta("+4", "cambiar-color", 4));
        }

        // Barajar el mazo 72+24+8= 104 cartas
        Collections.shuffle(mazo);
        repartir(mazo);
    }

    // Reparte las cartas a los dos jugaores y pone una sobre la mesa
    public void repartir(List<Carta> mazo) {
        for (int i = 0; i < 2; i++) {
            List<Carta> cartasJugador = new ArrayList<>();
            cartasJugadores.add(cartasJugador);
            for (int j = 0; j < 7; j++) {
                Carta carta = mazo.remove(0);
                cartasJugador.add(carta);
            }
        }

        ultimaCarta = mazo.remove(0);

    }

    /*
     * Este método pone las cartas sobre la mesa si se puede
     * He modificado los char a String para mayor claridad con los nombres y tipos
     */
    @Override
    public void poner(Map<String, Object> info, String idUser) throws Exception {
        String tipo = (String) info.get("tipo");
        Carta carta;
        if (tipo == "numerica") {
            int numero = (int) info.get("numero");
            String color = (String) info.get("color");
            carta = new Carta(numero, color);
        } else if (tipo == "sumar") {
            int cantidad = (int) info.get("cantidad");
            String color = (String) info.get("color");
            carta = new Carta(tipo, color, cantidad);
        } else {
            String color = (String) info.get("color");
            carta = new Carta(tipo, color);
        }
        if (!getJugadorConTurno().getId().equals(idUser))
            throw new MovimientoIlegalException("Al usuario con id " + idUser + " no le toca jugar");
        switch (carta.getTipo()) {
            case "numerica":
                if (carta.getColor().equals(this.ultimaCarta.getColor())
                        || carta.getNumero() == this.ultimaCarta.getNumero()) {
                    this.ultimaCarta = carta;
                    quitarCartaAJugador(carta, jugadorConTurno);
                    pasarTurno(carta);
                } else {
                    throw new MovimientoIlegalException("Esta carta no se puede jugar");
                }
                break;
            case "bloqueo":
                if (carta.getColor().equals(this.ultimaCarta.getColor())
                        || carta.getTipo().equals(this.ultimaCarta.getTipo())) {
                    this.ultimaCarta = carta;
                    quitarCartaAJugador(carta, jugadorConTurno);
                    pasarTurno(carta);
                    pasarTurno(carta);
                } else {
                    throw new MovimientoIlegalException("Esta carta no se puede jugar");
                }
                break;
            case "sentido":
                if (carta.getColor().equals(this.ultimaCarta.getColor())
                        || carta.getTipo().equals(this.ultimaCarta.getTipo())) {
                    this.ultimaCarta = carta;
                    sentido = !this.sentido;
                    quitarCartaAJugador(carta, jugadorConTurno);
                    pasarTurno(carta);
                } else {
                    throw new MovimientoIlegalException("Esta carta no se puede jugar");
                }
                break;
            case "sumar":
                if (carta.getTipo().equals(this.ultimaCarta.getTipo())
                        && carta.getCantidad() == this.ultimaCarta.getCantidad()) {
                    this.ultimaCarta = carta;
                    quitarCartaAJugador(carta, jugadorConTurno);

                    // Verificar si el siguiente jugador tiene cartas para sumar
                    if (tieneCartasParaSumar(jugadorConTurno, carta.getCantidad())) {
                        // Agregar las cartas al siguiente jugador y actualizar la última carta
                        agregarCartasASiguienteJugador(jugadorConTurno, carta.getCantidad());
                        cambiarUltimaCarta(carta);
                    } else {
                        // Acumular cartas en la última carta hasta que el siguiente jugador no disponga
                        // de ellas
                        acumularCartasEnUltimaCarta(jugadorConTurno, carta.getCantidad());
                    }

                    pasarTurno(carta);
                } else {
                    throw new NotImplementedException("Esta carta no se puede jugar");
                }
                break;

            case "cambia-color":
                throw new NotImplementedException("Esta funcionalidad aún no está implementada");
            default:
                throw new MovimientoIlegalException("No se ha encontrado una carta válida");
        }
    }

    @Override
    public void finalizar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'finalizar'");
    }

}
