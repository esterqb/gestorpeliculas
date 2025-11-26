package com.dam2.gestorpeliculas.DTO;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriticaDTO {
    private Long id;
    private String comentario;
    private int nota;
    private LocalDate fecha;
    private Long usuarioId;
    private Long peliculaId;
}
