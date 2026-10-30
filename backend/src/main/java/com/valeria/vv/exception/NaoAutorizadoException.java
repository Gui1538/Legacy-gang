package com.valeria.vv.exception;

/** Usuário não autenticado ou credenciais inválidas (vira HTTP 401). */
public class NaoAutorizadoException extends RuntimeException {
    public NaoAutorizadoException(String mensagem) {
        super(mensagem);
    }
}
