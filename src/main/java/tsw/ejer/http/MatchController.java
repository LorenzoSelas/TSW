package tsw.ejer.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tsw.ejer.dao.UserDAO;
import tsw.ejer.model.Partida;
import tsw.ejer.model.User;
import tsw.ejer.service.MatchService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("matches")
public class MatchController {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private MatchService matchService;

    @GetMapping("start")
    public Partida start(HttpSession session){
        User user = this.userDAO.findById(session.getAttribute("userId").toString()).get();
        return this.matchService.newMatch(user);
    }
    
    @PostMapping("poner")
    public Partida poner(HttpSession session, @RequestBody Map<String, Object> info){
        String id = info.get("id").toString();
        String idUser = session.getAttribute("userId").toString();
        char tipo = (char) info.get("tipo");

        if (tipo == 'n') {
            int numero = (int) info.get("numero");
            char color = (char) info.get("color");
            return this.matchService.poner(id, color, numero, idUser);
        } else if (tipo == 's') {
            int cantidad = (int) info.get("cantidad");
            char color = (char) info.get("color");
            return this.matchService.poner(id, tipo, color, cantidad, idUser);
        } else {
            char color = (char) info.get("color");
            return this.matchService.poner(id, tipo, color, idUser);
        }
    }

    @GetMapping("meToca")
    public boolean meToca(HttpSession session, @RequestParam String id){
        String idUser = session.getAttribute("userId").toString();
        Partida tab = this.matchService.findById(id);
        return tab.getJugadorConTurno().getId().equals(idUser);
    }
}
