package tsw.ejer.model;

import java.util.ArrayList;

public class Robot implements Runnable {
    private Tablero4r partida;
    User userBot;

    public Robot(Tablero4r p){
        this.partida = p;
        userBot = new User();
    }

    @Override
    public void run() {
        try {
            // Realiza la comprobación después de los 3 minutos
            Thread.sleep(300); //En Milisegundos
            if (comprobacion()) {
                // Aquí puedes agregar la lógica a ejecutar si la comprobación es verdadera
                System.out.println("La comprobación ha devuelto true.");
                this.partida.addUser(userBot);
                this.partida.iniciar();
            } else {
                // Aquí puedes agregar la lógica a ejecutar si la comprobación es falsa
                System.out.println("La comprobación ha devuelto false.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        
    }

    private boolean comprobacion(){
        return !(this.partida.getUsers().size() > 1);
    }

    private void genChildren(Tablero4r p, int depth){
        char [][] casillas = p.getCasillas();

    }
}
