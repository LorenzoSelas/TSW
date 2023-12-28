package tsw.ejer.model;

import java.util.ArrayList;
import java.util.List;

public class Mesa {
    private List<Carta> cartasJugadas;

    public Mesa() {
        this.cartasJugadas = new ArrayList<>();
    }

    public void jugarCarta(Carta carta) {
        cartasJugadas.add(carta);
    }

    public List<Carta> getCartasJugadas() {
        return cartasJugadas;
    }
    public void setCarta(Carta carta) {
        cartasJugadas.add(carta);
    }
}
