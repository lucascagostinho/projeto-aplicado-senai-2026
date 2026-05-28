package br.senai.apoiopet.exception;

public class AdocaoNotFoundException extends RuntimeException {
    public AdocaoNotFoundException(Long id) {
        super("Adoção não encontrada com id: " + id);
    }
}
