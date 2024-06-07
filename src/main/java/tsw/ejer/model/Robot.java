package tsw.ejer.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import tsw.ejer.dao.UserDAO;
import tsw.ejer.ws.IWSListener;
import tsw.ejer.ws.WSClient;

public class Robot implements Runnable {
    private UserDAO userDAO;
    private Tablero4r partida;
    User userBot;

    public Robot(Tablero4r p, UserDAO userDAO) {
        this.partida = p;
        this.userDAO = userDAO;
    }

    @Override
    public void run() {
        try {
            // Realiza la comprobación después de los 3 minutos
            Thread.sleep(3000); // En Milisegundos
            if (comprobacion()) {
                // Aquí puedes agregar la lógica a ejecutar si la comprobación es verdadera
                System.out.println("La comprobación ha devuelto true.");
                this.userBot = new User();
                this.userBot.setEmail(this.userBot.getId());
                this.userBot.setNombre(this.userBot.getId());
                this.userBot.setPassword(this.userBot.getId());
                this.userDAO.save(this.userBot);
                this.partida.addUser(this.userBot);
                this.partida.iniciar();

                try {
                    IWSListener listener = new IWSListener() {
                        @Override
                        public void notify(String message) {

                            // Parsear el mensaje como JSON si es necesario
                            try {
                                if (message.equals("your turn")) {
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
