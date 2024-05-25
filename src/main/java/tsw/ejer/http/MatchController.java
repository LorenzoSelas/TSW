package tsw.ejer.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import tsw.ejer.dao.UserDAO;
import tsw.ejer.model.Tablero;
import tsw.ejer.model.User;
import tsw.ejer.service.MatchService;
import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials="true")
@RestController
@RequestMapping("matches")
public class MatchController {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private MatchService matchService;

    @GetMapping("start")
    public Tablero start(HttpSession session, @RequestParam String tipo){
        User user;
        try{
            user = this.userDAO.findById(session.getAttribute("userId").toString()).get();
        } catch (Exception e){
            user = new User();
            user.setEmail(user.getId());
            user.setNombre(user.getId());
            user.setPassword(user.getId());
            this.userDAO.save(user);
            session.setAttribute("userId", user.getId());
        }
        try {
            return this.matchService.newMatch(user, tipo);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    //TODO arreglar error 500 al lanzar una petición de poner ficha en tablero. Necesaria la recuperación del id del usuario de la base de datos
    @PostMapping("poner/{id}")
    public Tablero poner(@PathVariable String id, HttpSession session, @RequestBody Map<String, Object> info){
        
    	//String idUser = info.get("userId").toString();
    	 User user;
         try{
             user = this.userDAO.findById(session.getAttribute("userId").toString()).get();
         } catch (Exception e){
             user = new User();
             user.setEmail(user.getId());
             user.setNombre(user.getId());
             user.setPassword(user.getId());
             this.userDAO.save(user);
             session.setAttribute("userId", user.getId());
         }
        return this.matchService.poner(id,user.getId(),info);
    }

    @GetMapping("meToca")
    public boolean meToca(HttpSession session, @RequestParam String id){
        String idUser = session.getAttribute("userId").toString();
        Tablero tab = this.matchService.findById(id);
        return tab.getJugadorConTurno().getId().equals(idUser);
    }

    @GetMapping("/{tipo}/ids")
    public ResponseEntity<String[]> getIds(@PathVariable String tipo) {
        String[] ids = this.matchService.getIds(tipo);
        if (ids.length > 0) {
            return ResponseEntity.ok(ids);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
