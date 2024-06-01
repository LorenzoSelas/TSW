package tsw.ejer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import tsw.ejer.ws.IWSListener;
import tsw.ejer.ws.WSClient;

public class Robot implements Runnable {
    private Tablero4r partida;
    User userBot;

    public Robot(Tablero4r p) {
        this.partida = p;
        userBot = new User();
    }

    @Override
    public void run() {
        try {
            // Realiza la comprobación después de los 3 minutos
            Thread.sleep(3000); // En Milisegundos
            if (comprobacion()) {
                // Aquí puedes agregar la lógica a ejecutar si la comprobación es verdadera
                System.out.println("La comprobación ha devuelto true.");
                this.partida.addUser(userBot);
                this.partida.iniciar();

                try {
                    IWSListener listener = new IWSListener() {
                        @Override
                        public void notify(String message) {
                            // Aquí puedes manejar el mensaje recibido del WebSocket
                            System.out.println("Mensaje recibido: " + message);

                            // Parsear el mensaje como JSON si es necesario
                            try {
                                JSONObject jsonMessage = new JSONObject(message);
                                // Manejar el JSON según la estructura de tu mensaje
                                String tipo = jsonMessage.getString("tipo");
                                if (tipo.equals("TURNO") && jsonMessage.getString("userId").equals(userBot.getId())) {
			                        Jugar();
                                }
                                // Agrega más tipos de mensajes según sea necesario
                            } catch (Exception e) {
                                System.err.println("Error al parsear el mensaje: " + e.getMessage());
                            }
                        }
                    };
                    WSClient wsClient = new WSClient("ws://localhost:8080/wsGames/" + this.partida.getId() + "?userId=" + userBot.getId(), listener);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                // Aquí puedes agregar la lógica a ejecutar si la comprobación es falsa
                System.out.println("La comprobación ha devuelto false.");
            }

            if (this.partida.getJugadorConTurno().getId().equals(this.userBot.getId())) {
                Jugar();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

    }

    protected void Jugar() {
        Map<String,Object> info = new HashMap<>();
        int col = new Random().nextInt(this.partida.getCasillas()[0].length);
        info.put("column", col);
        try{
            this.partida.poner(info, this.userBot.getId());
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private boolean comprobacion() {
        return !(this.partida.getUsers().size() > 1);
    }

    private void genChildren(Tablero4r p, int depth) {
        char[][] casillas = p.getCasillas();

    }
}
