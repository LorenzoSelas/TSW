package tsw.ejer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tsw.ejer.Excepcion.MovimientoIlegalException;
import tsw.ejer.Excepcion.NotImplementedException;
import tsw.ejer.model.Carta;
import tsw.ejer.model.Partida;
import tsw.ejer.model.User;

@Service
public class MatchService {
    private Map<String, Partida> tableros = new HashMap<String, Partida>();
    private List<Partida> tablerosPendientes = new ArrayList<>();

    public Partida newMatch(User user) {
        Partida tab;
        if (this.tablerosPendientes.isEmpty()){
            tab = new Partida();
            tab.addUser(user);
            this.tablerosPendientes.add(tab);
            this.tableros.put(tab.getId(),tab);
        } else {
            tab = tablerosPendientes.get(0);
            tab.addUser(user);
            this.tablerosPendientes.add(tab);
            tab.iniciar();
        }
        return tab;
    }

    /*
     * Metodo para poner carta numerica
     */
    public Partida poner(String id, char color, int numero, String idUser) {
        Partida partida = this.tableros.get(id);
        if (partida == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra esta partida");
        try{
            Carta carta = new Carta(numero, color);
            partida.poner(carta,idUser);
        }catch(MovimientoIlegalException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }catch(NotImplementedException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
        return partida;
    }

    /*
     * Metodo para poner carta de bloqueo, cambio de color o revertir orden
     */
    public Partida poner(String id, char tipo, char color, String idUser) {
        Partida partida = this.tableros.get(id);
        if (partida == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra esta partida");
        try{
            Carta carta = new Carta(tipo, color);
            partida.poner(carta,idUser);
        }catch(MovimientoIlegalException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }catch(NotImplementedException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
        return partida;
    }

    /*
     * Metodo para poner carta de suma
     */
    public Partida poner(String id, char tipo, char color, int cantidad, String idUser) {
        Partida partida = this.tableros.get(id);
        if (partida == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra esta partida");
        try{
            Carta carta = new Carta(tipo, color, cantidad);
            partida.poner(carta,idUser);
        }catch(MovimientoIlegalException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }catch(NotImplementedException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
        return partida;
    }


    public Partida findById(String id) {
        return this.tableros.get(id);
    }
    
}
