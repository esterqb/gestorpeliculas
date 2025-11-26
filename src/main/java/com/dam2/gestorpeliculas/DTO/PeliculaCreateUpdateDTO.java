package com.dam2.gestorpeliculas.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeliculaCreateUpdateDTO {
    private String titulo;
    private Integer duracion;
    private LocalDate fechaEstreno;
    private String sinopsis;
    private Integer valoracion;

    private Long fichaTecnicaId;
    private Long directorId;

    private List<Long> actoresIds;
    private List<Long> plataformasIds;
    private List<Long> idiomasIds;
    private List<Long> categoriasIds;
}
