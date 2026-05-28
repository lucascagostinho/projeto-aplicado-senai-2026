package br.senai.apoiopet.exception;

public class SolicitacaoNotFoundException extends RuntimeException {
    public SolicitacaoNotFoundException(Long id) {
        super("Solicitação não encontrada com id: " + id);
    }
}
