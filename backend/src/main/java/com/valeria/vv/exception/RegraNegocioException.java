package com.valeria.vv.exception;

/** Violação de regra de negócio (vira HTTP 400). */
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
