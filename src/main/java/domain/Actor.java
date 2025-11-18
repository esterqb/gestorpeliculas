package domain;


import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "actor")
@Data  // ✅ Lombok genera getters, setters, toString, equals, hashCode
@AllArgsConstructor      // ✅ genera constructor con todos los campos
@NoArgsConstructor
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String nombre;


    @ManyToMany
    @JoinTable(
            name="pelicula_actores",
            joinColumns = @JoinColumn(name="actor_id"),
            inverseJoinColumns = @JoinColumn(name="pelicula_id")
    )
    //ManyToMany(mappedBy = "actores")//1:N actor pelicula
    private List<Pelicula> peliculas;

    public void addPelicula(Pelicula pelicula) {
        peliculas.add(pelicula);
        pelicula.getActors().add(this);
    }
}
