package tsw.ejer.http;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import tsw.ejer.Excepcion.SQLInvalid;
import tsw.ejer.model.User;
import tsw.ejer.service.UserService;
import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;
    public static Map<String, User> usersByToken = new HashMap<>();
    public static Map<String, User> usersByName = new HashMap<>();
    public static Map<User, HttpSession> httpSessions = new HashMap<>();

    @PostMapping("register")
    public void register(@RequestBody Map<String, Object> info) {
        String nombre = info.get("nombre").toString().trim();
        String email = info.get("email").toString().trim();
        String pwd1 = info.get("pwd1").toString().trim();
        String pwd2 = info.get("pwd2").toString().trim();

        if (nombre.length() < 5)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El nombre no puede ser menor de 5 caracteres");

        if (pwd1.length() < 5)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La contraseña no puede ser menor de 5 caracteres");

        if (!pwd1.equals(pwd2))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Las contraseñas deben coincidir");
        try {
            this.userService.register(nombre, email, pwd1);
        } catch (SQLInvalid e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, exception.getMessage());
        }

    }

    @PutMapping("login")
    public void login(HttpSession session, @RequestBody Map<String, Object> info) {
        String email = info.get("email").toString().trim();
        String pwd = info.get("pwd").toString().trim();

        if (email.length() < 5)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El nombre no puede ser menor de 5 caracteres");

        if (pwd.length() < 5)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La contraseña no puede ser menor de 5 caracteres");

        if (this.userService.login(email, pwd) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario");

        User user = this.userService.login(email, pwd);
        session.setAttribute("userId", user.getId());
        usersByToken.put(user.getId(), user);
        usersByName.put(user.getNombre(), user);
    }

    @GetMapping("solicitarBorrado/{email}")
    public String solicitarBorrado(HttpSession session, @PathVariable String email, @RequestParam String pwd) {
        User user = this.userService.login(email, pwd);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Credenciales inválidas");
        }
        session.setAttribute("userId", user.getId());
        return "¿Estás Seguro de querer borrar esta cuenta?";
    }

    @DeleteMapping("borrarCuenta")
    public void borrarCuenta(HttpSession session, @RequestParam Boolean respuesta) {
        if (respuesta) {
            this.userService.borrar(session.getAttribute("userId").toString());
            session.invalidate();
        } else {
            session.removeAttribute("userId");
        }
    }
}
