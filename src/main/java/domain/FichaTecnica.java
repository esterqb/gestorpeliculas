package domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ficha_tecnica")
@Data  // ✅ Lombok genera getters, setters, toString, equals, hashCode
//@AllArgsConstructor      // ✅ genera constructor con todos los campos
@NoArgsConstructor
public class FichaTecnica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String director;
    private String sinopsis;
    private int duracion;
    private String pais;

    public FichaTecnica(Long id, String director, String sinopsis, int duracion, String pais) {
        this.id = id;
        this.director = director;
        this.sinopsis = sinopsis;
        this.duracion = duracion;
        this.pais = pais;
    }
}
