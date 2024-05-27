
package tsw.ejer.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(indexes = {
        @Index(columnList = "id", unique = true)
})
public class Partida {
    @Id
    @Column(length = 36)
    protected String id;

    @ManyToMany
    @JoinColumn(name = "user_id")
    protected List<User> users;

    @ManyToOne
    @JoinColumn(name = "ganador_id", nullable = false)
    protected User ganador;
    
    public Partida(String i, List<User> us, User gan){
        this.id=i;
        this.users=us;
        this.ganador=gan;
    }

}
