package br.senai.apoiopet.exception;

public class AnimalNotFoundException extends RuntimeException {

    public AnimalNotFoundException(Long id) {
        super("Animal não encontrado com id: " + id);
    }
}
