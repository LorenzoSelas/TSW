package tsw.ejer.dao;

import org.springframework.data.repository.CrudRepository;

import tsw.ejer.model.User;

public interface UserDAO extends CrudRepository<User, String>{
    User findByEmailAndPassword(String email, String password);
}
