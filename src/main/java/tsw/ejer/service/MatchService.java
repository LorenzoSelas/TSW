package tsw.ejer.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tsw.ejer.model.Tablero;
import tsw.ejer.model.Tablero4r;
import tsw.ejer.model.User;

@Service
public class MatchService {
    private Map<String, Tablero> tableros = new HashMap<String, Tablero>();
    private List<Tablero> tablerosPendientes = new ArrayList<>();

    public Tablero newMatch(User user, String juego) throws Exception{
        Tablero tab = null;
        if (this.tablerosPendientes.isEmpty()){
            Class<?> clazz;
            try{
                juego = "tsw.ejer.model." + juego;
                clazz = Class.forName(juego);
            }catch (ClassNotFoundException e){
                throw new Exception("El juego indicado no existe.");
            }
            Constructor<?> cons = clazz.getConstructors()[0];
            try {
                tab = (Tablero) cons.newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new Exception("Ha ocurrido un problema, contacta con el administrador si el problema persiste.");
            }
            
            tab.addUser(user);
            this.tablerosPendientes.add(tab);
            this.tableros.put(tab.getId(),tab);
        } else {
            tab = tablerosPendientes.get(0);
            tab.addUser(user);
            this.tablerosPendientes.remove(tab);
            tab.iniciar();
        }
        if (tab instanceof Tablero4r) {
            ((Tablero4r)tab).generarRobot();
        }
        return tab;
    }

    public Tablero poner(String id, String userId, Map<String, Object> info) {
        Tablero tablero = this.tableros.get(id);
        if (tablero == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra esta partida");
        try{
            tablero.poner(info,userId);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
        return tablero;
    }

    // TODO Realizar cambios en la clase getIds. Devolver los tipos de tableros, no el tablero genérico
    public String[] getIds(String tipo){
        String[] partidas = new String[tablerosPendientes.size()];
        for (int i=0; i<tablerosPendientes.size(); i++) {
            if (tablerosPendientes.get(i).getClass().getSimpleName().equals(tipo))
                partidas[i] = tablerosPendientes.get(i).getId();
        }
        return partidas;
    }

    public Tablero findById(String id) {
        return this.tableros.get(id);
    }
}
