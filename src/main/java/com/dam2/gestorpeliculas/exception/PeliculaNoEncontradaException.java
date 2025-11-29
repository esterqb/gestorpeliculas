package com.dam2.gestorpeliculas.exception;

public class PeliculaNoEncontradaException extends RuntimeException {
    public PeliculaNoEncontradaException(Long id) {
        super("La película con ID " + id + " no existe.");
    }
}
