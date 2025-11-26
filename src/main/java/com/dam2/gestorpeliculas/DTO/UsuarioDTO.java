package com.dam2.gestorpeliculas.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String username;
    private String email;
    private String rol;
}
