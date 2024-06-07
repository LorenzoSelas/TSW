package tsw.ejer.model;

import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import tsw.ejer.Excepcion.MovimientoIlegalException;
import tsw.ejer.Excepcion.TableNotInitializedException;
import tsw.ejer.dao.PartidaDAO;
import tsw.ejer.dao.UserDAO;
import tsw.ejer.ws.WSClient;

public class Tablero4r extends Tablero {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PartidaDAO pDAO;
    private WSClient wsClient;
    private char[][] casillas = new char[6][7];
    private char ultimoColor;
    private char ganador = Character.MIN_VALUE;
    private Robot robot;

    public void setDAO(PartidaDAO pDAO, UserDAO uDAO) {
        this.pDAO = pDAO;
        this.userDAO = uDAO;
    }

    public void generarRobot(){
        this.robot = new Robot(this, userDAO);

        Thread robotThread = new Thread(robot);
        robotThread.start();
    }

    public char[][] getCasillas() {
        return casillas;
    }

    @Override
    public void poner(Map<String, Object> info, String idUser) throws Exception {
        if (this.Iniciada == false)
            throw new TableNotInitializedException("La Partida:" + this.id + " no está inicializada aún.");
        int column = -1;
        
        try {
            column = (int) info.get("column");
        } catch (Exception e) {
            throw new Exception("Parametros no reconocidos para este tipo de juego.");
        }
        if (ganador != Character.MIN_VALUE)
            throw new MovimientoIlegalException("La partida esta finalizada, el ganador es: " + this.ganador);
        if (!jugadorConTurno.getId().equals(idUser))
            throw new MovimientoIlegalException("No es el turno de " + idUser);
        for (int i = 5; i >= 0; i--) {
            if (this.casillas[i][column] == '\0') {
                this.casillas[i][column] = ultimoColor;
                if (comprobarFin(i, column)){
                    ganador = ultimoColor;
                    finalizar();
                } else {
                    ultimoColor = this.ultimoColor == 'R' ? 'A' : 'R';
                    this.jugadorConTurno = this.jugadorConTurno == this.users.get(0) ? this.users.get(1)
                            : this.users.get(0);
                    this.wsClient = new WSClient("ws://localhost:8080/wsGames/" + this.id + "?userId=Tablero" + this.getId(), null);
                    JSONObject jso = new JSONObject().put("tipo", "TURNO").put("userId", this.jugadorConTurno.getId());
                    wsClient.sendMessage(jso);
                    JSONObject jsoPart = new JSONObject().put("tipo", "ACTUALIZACION").put("idTablero", this.getId()).put("Tablero", new JSONArray(this.getCasillas()));
                    wsClient.sendMessage(jsoPart);
                }
                return;
            }
        }
        throw new MovimientoIlegalException("Esta columna está llena");
    }

    private boolean comprobarFin(int fila, int columna) {
        // Verificar horizontalmente
        if (verificarLinea(fila, columna, 0, 1) || verificarLinea(fila, columna, 0, -1)) {
            return true;
        }

        // Verificar verticalmente
        if (verificarLinea(fila, columna, 1, 0)) {
            return true;
        }

        // Verificar diagonal hacia arriba (\)
        if (verificarLinea(fila, columna, 1, 1) || verificarLinea(fila, columna, -1, -1)) {
            return true;
        }

        // Verificar diagonal hacia abajo (/)
        if (verificarLinea(fila, columna, 1, -1) || verificarLinea(fila, columna, -1, 1)) {
            return true;
        }

        return false;
    }

    private boolean verificarLinea(int fila, int columna, int deltaFila, int deltaColumna) {
        int contador = 1;
        int nuevaFila = fila + deltaFila;
        int nuevaColumna = columna + deltaColumna;

        while (nuevaFila >= 0 && nuevaFila < 6 && nuevaColumna >= 0 && nuevaColumna < 7 &&
            casillas[nuevaFila][nuevaColumna] == ultimoColor) {
            contador++;
            nuevaFila += deltaFila;
            nuevaColumna += deltaColumna;
        }

        nuevaFila = fila - deltaFila;
        nuevaColumna = columna - deltaColumna;

        while (nuevaFila >= 0 && nuevaFila < 6 && nuevaColumna >= 0 && nuevaColumna < 7 &&
            casillas[nuevaFila][nuevaColumna] == ultimoColor) {
            contador++;
            nuevaFila -= deltaFila;
            nuevaColumna -= deltaColumna;
        }

        return contador >= 4;
    }

    @Override
    public void iniciar() {
        this.jugadorConTurno = this.users.get(new Random().nextInt(this.users.size()));
        ultimoColor = 'R';
        this.Iniciada = true;
        
    }
    //TODO no guarda la partida en bbdd a pesar de añadir los campos al robot. Mirar si existe previamente en la tabla users
    public void finalizar() {
        this.ganador=this.ultimoColor;
        if (this.users.get(0).getId().equals(this.users.get(0).getEmail()) && this.users.get(1).getId().equals(this.users.get(1).getEmail()))
            return;
        Partida partida = new Partida(id, users, jugadorConTurno);
        try {
            this.userDAO.findById(users.get(0).getId());
        } catch (Exception e) {
            this.userDAO.save(users.get(0));
            e.printStackTrace();
        }
        try {
            this.userDAO.findById(users.get(1).getId());
        } catch (Exception e) {
            this.userDAO.save(users.get(1));
            e.printStackTrace();
        }
        try {
            this.pDAO.save(partida);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
