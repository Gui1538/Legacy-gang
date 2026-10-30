package com.valeria.vv.exception;

/** Registro não existe (vira HTTP 404). */
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
