package tsw.ejer.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
@Entity
@Table(indexes = {
    @Index(columnList = "email", unique = true)
})
public class User {
    @Id @Column(length = 36)
    private String id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String password;
    
    public User(){
        this.id = UUID.randomUUID().toString();
    }

    public void setNombre(String n){
        this.nombre = n;
    }

    public void setEmail(String e){
        this.email = e;
    }

    public void setPasswword(String p){
        this.password = org.apache.commons.codec.digest.DigestUtils.sha512Hex(p);
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPasswword(){
        return this.password;
    }

    public String getId(){
        return id;
    }
}
