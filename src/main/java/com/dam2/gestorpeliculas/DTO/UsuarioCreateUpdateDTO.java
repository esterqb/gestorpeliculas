package com.dam2.gestorpeliculas.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCreateUpdateDTO {
    private String username;
    private String email;
    private String password;
    private String rol;
}
