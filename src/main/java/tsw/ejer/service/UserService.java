package tsw.ejer.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tsw.ejer.Excepcion.SQLInvalid;
import tsw.ejer.dao.UserDAO;
import tsw.ejer.model.User;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public void register(String nombre, String email, String pwd1) throws SQLInvalid{
        User user = new User();
        user.setNombre(nombre);
        user.setEmail(email);
        user.setPassword(pwd1);

        try {
            this.userDAO.save(user);
        }catch (Exception e) {
            Throwable t = e.getCause();
            SQLException exp = null;
            while ((t != null) && !(t instanceof SQLException)) {
                t = t.getCause();
                exp = (SQLException) t;  
            }
            if (exp.getSQLState().equals("23000")) {
                throw new SQLInvalid("Valor duplicado"); 
            }else if (exp.getSQLState().equals("22001"))
                throw new SQLInvalid("Valor demasiado largo");
            else
                throw new SQLInvalid("Hubo un problema con la Base de Datos"); 
        }
    }

    public User login(String email, String pwd) {
        pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
        return this.userDAO.findByEmailAndPassword(email, pwd);
    }

    public void borrar(String id) {
        this.userDAO.deleteById(id);
    } 
}