package tsw.ejer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Tablero {
    protected String id;
    protected User jugadorConTurno;
    protected List<User> users;

    public Tablero(){
        id = UUID.randomUUID().toString();
        this.users = new ArrayList<>();
    }

    public String getId() {
        return id;
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

    public abstract void poner(Map<String, Object> info, String idUser) throws Exception;
    public abstract void iniciar();
    public abstract void finalizar();
}
