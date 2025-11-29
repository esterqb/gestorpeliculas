package com.dam2.gestorpeliculas.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeliculaCreateUpdateDTO {
    @NotBlank(message="El título no puede estar vacío.")
    private String titulo;
    @Min(value=1,message="Duración mínima de 1 minuto.")
    private Integer duracion;
    @PastOrPresent(message="La fecha no puede ser futura.")
    private LocalDate fechaEstreno;
    @Size(max=500, message="La sinopsis no puede superar 500 caracteres.")
    private String sinopsis;
    @Min(0)
    @Max(10)
    private Integer valoracion;

    @NotNull(message="Debe tener director.")
    private Long directorId;

    @NotEmpty(message="Debe tener actores")
    private List<Long> actoresIds;
    @NotEmpty(message="Debe estar en alguna plataforma")
    private List<Long> plataformasIds;
    @NotEmpty(message="Debe tener algún idioma")
    private List<Long> idiomasIds;
    @NotEmpty(message="Debe pertenecer a alguna categoría")
    private List<Long> categoriasIds;
}
