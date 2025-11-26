package com.dam2.gestorpeliculas.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichaTecnicaDTO {
    private Long id;
    private Integer duracion;
    private String pais;
    private Long directorId;
}
