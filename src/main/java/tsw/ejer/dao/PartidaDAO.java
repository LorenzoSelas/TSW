package tsw.ejer.dao;

import org.springframework.data.repository.CrudRepository;

import tsw.ejer.model.Partida;
import tsw.ejer.model.User;

public interface PartidaDAO extends CrudRepository<Partida, String>{
    Partida findByGanador(User ganador);
}
