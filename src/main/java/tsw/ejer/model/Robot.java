package tsw.ejer.model;

public class Robot implements Runnable {
    private Tablero4r partida;

    public Robot(Tablero4r p){
        this.partida = p;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(30000); //En Milisegundos
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        // Realiza la comprobación después de los 3 minutos
        if (comprobacion()) {
            // Aquí puedes agregar la lógica a ejecutar si la comprobación es verdadera
            System.out.println("La comprobación ha devuelto true.");
        } else {
            // Aquí puedes agregar la lógica a ejecutar si la comprobación es falsa
            System.out.println("La comprobación ha devuelto false.");
        }
    }

    private boolean comprobacion(){
        return !(this.partida.getUsers().size() > 1);
    }
}
