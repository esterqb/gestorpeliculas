package com.dam2.gestorpeliculas.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichaTecnicaCreateUpdateDTO {
    private Integer duracion;
    private String pais;
    private Long directorId;
}
