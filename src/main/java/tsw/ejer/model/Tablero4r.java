package tsw.ejer.model;

import java.util.Map;
import java.util.Random;

import tsw.ejer.Excepcion.MovimientoIlegalException;
//import lombok.Data;

//@Data
public class Tablero4r extends Tablero {
    private char[][] casillas = new char[6][7];
    private char ultimoColor;
    private char ganador = Character.MIN_VALUE;

    public char[][] getCasillas() {
        return casillas;
    }

    @Override
    public void poner(Map<String, Object> info, String idUser) throws Exception {
        int column = -1;
        try {
            column = (int) info.get("columna");
        } catch (Exception e) {
            throw new Exception("Parametros no reconocidos para este tipo de juego.");
        }
        if (ganador != Character.MIN_VALUE)
            throw new MovimientoIlegalException("La partida esta finalizada, el ganador es: " + this.ganador);
        if (!jugadorConTurno.getId().equals(idUser))
            throw new MovimientoIlegalException("No es el turno de " + idUser);
        for (int i = 5; i > 0; i--) {
            if (this.casillas[i][column] == '\0') {
                this.casillas[i][column] = ultimoColor;
                if (comprobarFin(i, column)){
                    ultimoColor = this.ultimoColor == 'R' ? 'A' : 'R';
                    this.jugadorConTurno = this.jugadorConTurno == this.users.get(0) ? this.users.get(1)
                            : this.users.get(0);
                }
                return;
            }
        }
        throw new MovimientoIlegalException("Esta columna está llena");
    }

    private boolean comprobarFin(int fila, int columna) {
        if (verificarLinea(fila, 0, 0, 1)) {
            return false;
        }

        // Verificar verticalmente
        if (verificarLinea(0, columna, 1, 0)) {
            return false;
        }

        // Verificar diagonal hacia arriba (\)
        if (verificarLinea(fila, columna, -1, -1)) {
            return false;
        }

        // Verificar diagonal hacia abajo (/)
        if (verificarLinea(fila, columna, 1, -1)) {
            return false;
        }

        finalizar();
        return false;
    }

    private boolean verificarLinea(int fila, int columna, int deltaFila, int deltaColumna) {
        int contador = 0;
        for (int i = 0; i < 4; i++) {
            int nuevaFila = fila + i * deltaFila;
            int nuevaColumna = columna + i * deltaColumna;

            if (nuevaFila >= 0 && nuevaFila < 6 && nuevaColumna >= 0 && nuevaColumna < 7 &&
                casillas[nuevaFila][nuevaColumna] == ultimoColor) {
                contador++;
            } else {
                break; // Interrumpir la verificación si no hay una coincidencia en esta dirección
            }
        }
        return contador == 4;
    }

    @Override
    public void iniciar() {
        this.jugadorConTurno = this.users.get(new Random().nextInt(this.users.size()));
        ultimoColor = 'R';
    }

    @Override
    public void finalizar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'finalizar'");
    }
}
